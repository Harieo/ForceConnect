package uk.co.harieo.forceop.spigot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import net.md_5.bungee.api.ChatColor;

public class PluginConfig {

	private boolean verbose = true;
	private boolean failsafe = true;
	private String connectionRefusedMessage =
			ChatColor.RED + "Your connection is insecure. Please make sure you are using the correct IP address.";

	private String hashingAlgorithm = "SHA-256";

	/**
	 * Handler to read this plugin's configuration
	 *
	 * @param plugin which is running
	 */
	public PluginConfig(JavaPlugin plugin) {
		try {
			FileConfiguration configuration = getConfigFile(plugin);
			verbose = configuration.getBoolean("verbose");
			failsafe = configuration.getBoolean("failsafe");

			String rawConnectionRefusedMessage = configuration.getString("connection-refused-message");
			if (rawConnectionRefusedMessage != null) {
				connectionRefusedMessage = ChatColor.translateAlternateColorCodes('&', rawConnectionRefusedMessage);
			}

			hashingAlgorithm = configuration.getString("hashing-algorithm");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the configuration file or creates it from a matching resource
	 *
	 * @param plugin which holds the configuration file
	 * @return the configuration file
	 * @throws IOException if an error occurs creating the file
	 */
	private FileConfiguration getConfigFile(JavaPlugin plugin) throws IOException {
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.exists()) {
			if (!dataFolder.mkdir()) {
				throw new IOException("Failed to create plugin folder");
			}
		}

		File configFile = new File(dataFolder, "config.yml");
		if (!configFile.exists()) {
			try (InputStream stream = plugin.getResource("config.yml")) {
				if (stream != null) {
					Files.copy(stream, configFile.toPath());
				} else {
					throw new IOException("Resource unavailable: config.yml");
				}
			}
		}

		return YamlConfiguration.loadConfiguration(configFile);
	}

	/**
	 * @return whether to send verbose logging to console
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @return whether to lock down the server if there's a security error
	 */
	public boolean isFailsafe() {
		return failsafe;
	}

	/**
	 * @return the message sent to players when their connection is refused
	 */
	public String getConnectionRefusedMessage() {
		return connectionRefusedMessage;
	}

	/**
	 * @return the algorithm used to hash the token file
	 */
	public String getHashingAlgorithm() {
		return hashingAlgorithm;
	}

}
