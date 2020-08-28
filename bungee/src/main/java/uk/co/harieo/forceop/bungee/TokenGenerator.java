package uk.co.harieo.forceop.bungee;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TokenGenerator {

	public static final String DEFAULT_ALGORITHM = "SHA-256";

	private static final int tokenLength = 32;

	private final SecureRandom random = new SecureRandom();
	private final String hashingAlgorithm;

	/**
	 * Generator for creating and hashing connection keys
	 *
	 * @param hashingAlgorithm which should be used to hash the token
	 */
	public TokenGenerator(String hashingAlgorithm) {
		this.hashingAlgorithm = verifyHashingAlgorithm(hashingAlgorithm);
	}

	/**
	 * Generates a new token
	 *
	 * @return the new token
	 */
	public byte[] nextToken() {
		byte[] bytes = new byte[tokenLength];
		for (int i = 0; i < tokenLength; i++) {
			random.nextBytes(bytes);
		}
		return bytes;
	}

	/**
	 * Hashes a token using the provided hashing algorithm
	 *
	 * @param token which should be hashed
	 * @return the hashed byte array of the token
	 */
	public byte[] hash(byte[] token) {
		try {
			MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
			return digest.digest(token);
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("Failed to hash token", e);
		}
	}

	/**
	 * Verifies that the specified hashing algorithm is available to this system and in the event it's not, returns
	 * the default algorithm instead
	 *
	 * @param requestedAlgorithm which should be prioritised over the default
	 * @return the requested algorithm if it is valid or the default algorithm
	 */
	private String verifyHashingAlgorithm(String requestedAlgorithm) {
		try {
			MessageDigest.getInstance(requestedAlgorithm);
			return requestedAlgorithm;
		} catch (NoSuchAlgorithmException ignored) {
			System.out.println(
					"Failed to digest hashing algorithm: " + requestedAlgorithm + " - Switching to default...");
			return DEFAULT_ALGORITHM;
		}
	}

}
