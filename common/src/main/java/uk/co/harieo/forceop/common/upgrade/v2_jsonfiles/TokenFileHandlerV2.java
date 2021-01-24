package uk.co.harieo.forceop.common.upgrade.v2_jsonfiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

// a v2 frozen version of TokenFileHandler
public class TokenFileHandlerV2 {

	public static final Charset ENCODING = StandardCharsets.UTF_8;
	public static final String SERVER_KEY_FILE_NAME = "server.publickey.json";
	public static final String PROXY_KEY_FILE_NAME = "proxy.privatekey.json";

	private final Path serverKeyPath;
	private final Path proxyKeyPath;

	private final Gson gson;

	/**
	 * A handler which reads and writes token files. Note: The unedited token will not work here for the purpose of
	 * security, instead the file will only write hashed byte arrays.
	 *
	 * @param baseDirectory where the files should be stored
	 */
	public TokenFileHandlerV2(Path baseDirectory) {
		this.serverKeyPath = baseDirectory.resolve(SERVER_KEY_FILE_NAME);
		this.proxyKeyPath = baseDirectory.resolve(PROXY_KEY_FILE_NAME);

		this.gson = new GsonBuilder()
				.disableHtmlEscaping()
				.create();
	}

	/**
	 * Writes the hashed form of the token to the file
	 *
	 * @param hash to be written to the file
	 * @throws IOException generated when there is a writing error
	 */
	public void writeServerKey(byte[] hash) throws IOException {
		String base64 = Base64.getEncoder().encodeToString(hash);
		KeyFileV2 file = new KeyFileV2(base64, KeyFileV2.SERVER_KEY_COMMENT);

		// use a try-with-resources to auto-close but let exceptions continue upward
		try (BufferedWriter writer = Files.newBufferedWriter(serverKeyPath, ENCODING, StandardOpenOption.CREATE)) {
			gson.toJson(file, writer);
		}
	}

	/**
	 * Loads the hashed token as a string from the file
	 *
	 * @return the loaded hash
	 * @throws IOException generated when there is a reading error
	 */
	public byte[] readServerKey() throws IOException {
		// use a try-with-resources to auto-close but let exceptions continue upward
		try (BufferedReader reader = Files.newBufferedReader(serverKeyPath)) {
			KeyFileV2 file = gson.fromJson(reader, KeyFileV2.class);
			return file.getKey().getBytes(ENCODING);
		}
	}

	/**
	 * Writes the token into a private key file which is secured only by Base64 encoding (easy to crack in seconds)
	 *
	 * @param token to be encoded
	 * @throws IOException generated when there is a writing error
	 */
	public void writeProxyKey(String token) throws IOException {
		String base64 = Base64.getEncoder().encodeToString(token.getBytes(ENCODING));
		KeyFileV2 file = new KeyFileV2(base64, KeyFileV2.PROXY_KEY_COMMENT);

		// use a try-with-resources to auto-close but let exceptions continue upward
		try (BufferedWriter writer = Files.newBufferedWriter(proxyKeyPath, ENCODING, StandardOpenOption.CREATE)) {
			gson.toJson(file, writer);
		}
	}

	/**
	 * Loads and decodes the token from the private key file
	 *
	 * @return the unsecure full token
	 * @throws IOException generated when there is a read error
	 */
	public String readProxyKey() throws IOException {
		// use a try-with-resources to auto-close but let exceptions continue upward
		try (BufferedReader reader = Files.newBufferedReader(proxyKeyPath, ENCODING)) {
			KeyFileV2 file = gson.fromJson(reader, KeyFileV2.class);
			return file.getKey();
		}
	}

	/**
	 * @return the path where the hash is saved
	 */
	public Path getServerKeyPath() {
		return serverKeyPath;
	}

	/**
	 * @return whether the token's hash file currently exists
	 */
	public boolean serverKeyExists() {
		return Files.exists(serverKeyPath);
	}

	/**
	 * @return the path when there private token is saved
	 */
	public Path getProxyKeyPath() {
		return proxyKeyPath;
	}

	/**
	 * @return whether the base64 token file currently exists
	 */
	public boolean proxyKeyExists() {
		return Files.exists(proxyKeyPath);
	}
}
