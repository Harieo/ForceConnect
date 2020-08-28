package uk.co.harieo.forceop.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import uk.co.harieo.forceop.common.TokenFileHandler;

public class ForceOP extends JavaPlugin implements Listener, PluginMessageListener {

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
			getLogger().info("ForceOP Shield is now verifying connections from players!");
		} else if (pluginConfig.isFailsafe()) {
			getLogger().warning(
					"ForceOP Shield is REJECTING all connections due to a security error. "
							+ "You can disable this failsafe in the config (not recommended).");
		} else {
			getLogger()
					.warning("ForceOP Shield is NOT verifying connections from players. Your server is not secured.");
		}

		getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	private void verboseLog(String message) {
		if (pluginConfig.isVerbose()) {
			getLogger().info(message);
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		if (channel.equals(CHANNEL)) {
			hash = bytes;
			verboseLog("Received a new ForceOP Shield security key...");
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		boolean allowLogin = !pluginConfig.isFailsafe();
		if (enabled) {
			String hostname = event.getHostname();

			try {
				MessageDigest digest = MessageDigest.getInstance(pluginConfig.getHashingAlgorithm());
				if (Arrays.equals(digest.digest(hostname.getBytes(StandardCharsets.UTF_8)), hash)) {
					allowLogin = true;
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (!allowLogin) {
			event.disallow(Result.KICK_OTHER, pluginConfig.getConnectionRefusedMessage());
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
		TokenFileHandler tokenFileHandler = new TokenFileHandler(
				new File(getDataFolder(), TokenFileHandler.DEFAULT_FILE_NAME));
		// Enabled is set to true if the hashing algorithm was accepted so if there's an error, it must be set to false here
		if (tokenFileHandler.exists()) {
			try {
				hash = tokenFileHandler.readHash();
				getLogger().info("Loaded hash file successfully");
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

}
