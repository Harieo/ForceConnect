package uk.co.harieo.forceop.bungee;

import java.io.IOException;
import javax.xml.bind.DatatypeConverter;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import uk.co.harieo.forceop.common.TokenFileHandler;

public class ForceConnect extends Plugin implements Listener {

	private PluginConfig pluginConfig;
	private TokenGenerator generator;
	private TokenFileHandler tokenFileHandler;
	private String token;

	@Override
	public void onEnable() {
		pluginConfig = new PluginConfig(this);
		generator = new TokenGenerator(pluginConfig.getHashingAlgorithm());
		tokenFileHandler = new TokenFileHandler(getDataFolder());

		if (tokenFileHandler.base64Exists()) {
			verboseLog("A token file already exists. If you need a new one, use /forceconnect generate");
			try {
				token = tokenFileHandler.readToken();
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().severe("Failed to read private key, this is a fatal error.");
			}
		} else {
			generateToken();
		}

		PluginManager manager = getProxy().getPluginManager();
		manager.registerListener(this, this);
		manager.registerCommand(this, new ForceConnectCommand(this));
	}

	/**
	 * Generates a new token, hashes it then saves the hash to a file
	 */
	void generateToken() {
		try {
			verboseLog("Generating a new secure key...");
			byte[] tokenArray = generator.nextToken();
			token = convertToHex(tokenArray);
			verboseLog("Key generated, attempting to hash...");
			byte[] hash = generator.hash(DatatypeConverter.parseHexBinary(token));
			verboseLog("Hashed successfully. Saving to file...");
			tokenFileHandler.writeHash(hash);
			tokenFileHandler.writeToken(token);
			getLogger()
					.info("Your token file has been generated at path: "
							+ tokenFileHandler.getTokenHashFile().getPath());
		} catch (SecurityException e) {
			e.printStackTrace();
			getLogger().severe("There has been a security failure generating the token");
		} catch (IOException e) {
			e.printStackTrace();
			getLogger().severe("Unable to write hashed token to file");
		}
	}

	/**
	 * Sends a message via the logger if verbose mode is enabled
	 *
	 * @param message to send to console
	 */
	private void verboseLog(String message) {
		if (pluginConfig.isVerbose()) {
			getLogger().info(message);
		}
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts an array of bytes into hex in string format
	 *
	 * @param bytes to convert to hex
	 * @return the hex encoded string
	 */
	private String convertToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	@EventHandler
	public void onHandshake(PlayerHandshakeEvent event) {
		event.getHandshake().setHost(token);
	}

}
