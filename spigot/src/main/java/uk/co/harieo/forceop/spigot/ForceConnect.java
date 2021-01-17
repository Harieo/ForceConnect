package uk.co.harieo.forceop.spigot;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import uk.co.harieo.forceop.common.DataConverter;
import uk.co.harieo.forceop.common.TokenFileHandler;

public class ForceConnect extends JavaPlugin implements Listener {

	public static final String CHANNEL = "forceop:firewall";

	private PluginConfig pluginConfig;
	private boolean enabled = false;
	private byte[] hash;

	@Override
	public void onEnable() {
		pluginConfig = new PluginConfig(this);
		if (isHashingAlgorithmValid(pluginConfig.getHashingAlgorithm())) {
			verboseLog("The hashing algorithm has been accepted");
			enabled = true;
		} else {
			getLogger().severe("Failed to load hashing algorithm, this will result in a security failure. "
					+ "Please check your config.yml to ensure algorithm matches your proxy.");
		}

		loadHash();

		if (enabled) {
			getLogger().info("ForceConnect is now verifying connections from players!");
		} else if (pluginConfig.isFailsafe()) {
			getLogger().warning(
					"ForceConnect is REJECTING all connections due to a security error. "
							+ "You can disable this failsafe in the config (not recommended).");
		} else {
			getLogger()
					.warning("ForceConnect is NOT verifying connections from players. Your server is not secured.");
		}

		Bukkit.getPluginManager().registerEvents(this, this);
	}

	/**
	 * Logs a message to console if the user has enabled verbose mode
	 *
	 * @param message to be logged to console
	 */
	private void verboseLog(String message) {
		if (pluginConfig.isVerbose()) {
			getLogger().info(message);
		}
	}

	/**
	 * Checks whether the hashing algorithm provided in the configuration file
	 *
	 * @param algorithm which was provided in the configuration file
	 * @return whether the hashing algorithm is valid
	 */
	private boolean isHashingAlgorithmValid(String algorithm) {
		try {
			MessageDigest.getInstance(algorithm);
			return true;
		} catch (NoSuchAlgorithmException ignored) {
			return false;
		}
	}

	/**
	 * Attempts to load the hash from the token file
	 */
	private void loadHash() {
		TokenFileHandler tokenFileHandler = new TokenFileHandler(getDataFolder());
		if (tokenFileHandler.base64Exists()) { // Warn the user if they've uploaded the wrong file
			getLogger().severe("The private key has been detected on this server. "
					+ "It is a severe security risk and should only be located on the proxy.");
		}

		// Enabled is set to true if the hashing algorithm was accepted so if there's an error, it must be set to false here
		if (tokenFileHandler.hashExists()) {
			try {
				hash = tokenFileHandler.readHash();
				verboseLog("Loaded hash file successfully");
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().warning("Failed to read hash, there has been an error.");
				enabled = false;
			}
		} else {
			getLogger().warning("There is no hash file in the plugin folder. Generate one on your proxy server!");
			enabled = false;
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		boolean allowLogin = !pluginConfig.isFailsafe();
		if (enabled) {
			String hostname = event.getHostname().split(":")[0];

			try {
				MessageDigest digest = MessageDigest.getInstance(pluginConfig.getHashingAlgorithm());
				if (Arrays.equals(digest.digest(DataConverter.convertHexToBinary(hostname)), hash)) {
					allowLogin = true;
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException ignored) { } // Generated when the hostname isn't a hex
		}

		if (!allowLogin) {
			event.disallow(Result.KICK_OTHER, pluginConfig.getConnectionRefusedMessage());
		}
	}

}
