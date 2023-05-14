package uk.co.harieo.forceconnect.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PluginConfig {

	private boolean verbose = true;
	private String failsafeMessage =
			ChatColor.RED + "This server is not accepting connections at this time, please contact an Administrator";

	private String hashingAlgorithm = "SHA-256";

	public PluginConfig(Plugin plugin) {
		try {
			Configuration configuration = getConfigFile(plugin);
			verbose = configuration.getBoolean("verbose");
			failsafeMessage = ChatColor.translateAlternateColorCodes('&', configuration.getString("failsafe-message"));
			hashingAlgorithm = configuration.getString("hashing-algorithm");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Configuration getConfigFile(Plugin plugin) throws IOException {
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.exists()) {
			if (!dataFolder.mkdir()) {
				throw new IOException("Failed to create plugin folder");
			}
		}

		File configFile = new File(dataFolder, "config.yml");
		if (!configFile.exists()) {
			try (InputStream stream = plugin.getResourceAsStream("config.yml")) {
				if (stream != null) {
					Files.copy(stream, configFile.toPath());
				} else {
					throw new IOException("Resource unavailable: config.yml");
				}
			}
		}

		return ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
	}

	public boolean isVerbose() {
		return verbose;
	}

	public String getFailsafeMessage() {
		return failsafeMessage;
	}

	public String getHashingAlgorithm() {
		return hashingAlgorithm;
	}

}
