package uk.co.harieo.forceop.bungee;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

public class TokenGenerator {

	public static final String DEFAULT_ALGORITHM = "SHA-256";

	private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String possibilities = alphabet + alphabet.toLowerCase(Locale.ENGLISH) + "0123456789";
	private static final int tokenLength = 21;

	private final SecureRandom random = new SecureRandom();
	private final char[] possibilityArray = possibilities.toCharArray();
	private final String hashingAlgorithm;

	public TokenGenerator(String hashingAlgorithm) {
		this.hashingAlgorithm = verifyHashingAlgorithm(hashingAlgorithm);
	}

	public String nextToken() {
		char[] buffer = new char[tokenLength];
		for (int i = 0; i < tokenLength; i++) {
			buffer[i] = possibilityArray[random.nextInt(possibilityArray.length)];
		}
		return new String(buffer);
	}

	public byte[] hash(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
			return digest.digest(token.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException("Failed to hash token", e);
		}
	}

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
