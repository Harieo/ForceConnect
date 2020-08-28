package uk.co.harieo.forceop.common;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

public class TokenFileHandler {

	public static final Charset ENCODING = StandardCharsets.UTF_8;
	public static final String DEFAULT_HASH_FILE_NAME = "public_key";
	public static final String DEFAULT_TOKEN_FILE_NAME = "private_key";

	private final File tokenHashFile;
	private final File tokenBase64File;

	/**
	 * A handler which reads and writes token files. Note: The unedited token will not work here for the purpose of
	 * security, instead the file will only write hashed byte arrays.
	 *
	 * @param directory where the files should be stored
	 */
	public TokenFileHandler(File directory) {
		this.tokenHashFile = new File(directory, DEFAULT_HASH_FILE_NAME);
		this.tokenBase64File = new File(directory, DEFAULT_TOKEN_FILE_NAME);
	}

	/**
	 * Writes the hashed form of the token to the file
	 *
	 * @param hash to be written to the file
	 * @throws IOException generated when there is a writing error
	 */
	public void writeHash(byte[] hash) throws IOException {
		try (FileOutputStream fileOutputStream = new FileOutputStream(tokenHashFile)) {
			fileOutputStream.write(hash);
		}
	}

	/**
	 * Loads the hashed token as a string from the file
	 *
	 * @return the loaded hash
	 * @throws IOException generated when there is a reading error
	 */
	public byte[] readHash() throws IOException {
		return Files.readAllBytes(tokenHashFile.toPath());
	}

	/**
	 * Writes the token into a private key file which is secured only by Base64 encoding (easy to crack in seconds)
	 *
	 * @param token to be encoded
	 * @throws IOException generated when there is a writing error
	 */
	public void writeToken(String token) throws IOException {
		String base64 = Base64.getEncoder().encodeToString(token.getBytes(ENCODING));
		writeToFile(tokenBase64File, base64);
	}

	/**
	 * Loads and decodes the token from the private key file
	 *
	 * @return the unsecure full token
	 * @throws IOException generated when there is a read error
	 */
	public String readToken() throws IOException {
		return new String(Base64.getDecoder().decode(Files.readAllBytes(tokenBase64File.toPath())), ENCODING);
	}

	/**
	 * Writes the given string to the given file
	 *
	 * @param file to write the string to
	 * @param toWrite to write to the file
	 * @throws IOException generated when there is a writing error
	 */
	private void writeToFile(File file, String toWrite) throws IOException {
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(toWrite);
		}
	}

	/**
	 * @return the file where the hash is saved
	 */
	public File getTokenHashFile() {
		return tokenHashFile;
	}

	/**
	 * @return whether the token's hash file currently exists
	 */
	public boolean hashExists() {
		return tokenHashFile.exists();
	}

	/**
	 * @return the file when there private token is saved
	 */
	public File getTokenBase64File() {
		return tokenBase64File;
	}

	/**
	 * @return whether the base64 token file currently exists
	 */
	public boolean base64Exists() {
		return tokenBase64File.exists();
	}

}
