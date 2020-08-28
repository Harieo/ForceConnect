package uk.co.harieo.forceop.common;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TokenFileHandler {

	public static final Charset ENCODING = StandardCharsets.UTF_8;
	public static final String DEFAULT_FILE_NAME = "token";

	private final File tokenFile;

	/**
	 * A handler which reads and writes token files. Note: The unedited token will not work here for the purpose of
	 * security, instead the file will only write hashed byte arrays.
	 *
	 * @param file which represents the token file
	 */
	public TokenFileHandler(File file) {
		this.tokenFile = file;
	}

	/**
	 * Writes the hashed form of the token to the file
	 *
	 * @param hash to be written to the file
	 * @throws IOException generated when there is a writing error
	 */
	public void writeHash(byte[] hash) throws IOException {
		try (FileWriter writer = new FileWriter(tokenFile)) {
			writer.write(new String(hash, ENCODING));
		}
	}

	/**
	 * Loads the hashed token as a string from the file
	 *
	 * @return the loaded hash
	 * @throws IOException generated when there is a reading error
	 */
	public byte[] readHash() throws IOException {
		return Files.readAllBytes(tokenFile.toPath());
	}

	/**
	 * @return the token file, which may or may not be created
	 */
	public File getTokenFile() {
		return tokenFile;
	}

	/**
	 * @return whether the token file currently exists
	 */
	public boolean exists() {
		return tokenFile.exists();
	}

}
