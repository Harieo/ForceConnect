package uk.co.harieo.forceop.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class TokenFileHandler {

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
	public TokenFileHandler(Path baseDirectory) {
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
		ServerKeyFile file = new ServerKeyFile(2, base64);

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
			ServerKeyFile file = gson.fromJson(reader, ServerKeyFile.class);
			return file.getHash().getBytes(ENCODING);
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
		ProxyKeyFile file = new ProxyKeyFile(2, base64);

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
			ProxyKeyFile file = gson.fromJson(reader, ProxyKeyFile.class);
			return file.getToken();
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
