package uk.co.harieo.forceop.common;

import java.math.BigInteger;

public class DataConverter {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Converts an array of bytes into hex in string format
	 *
	 * @param bytes to convert to hex
	 * @return the hex encoded string
	 */
	public static String convertBytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Converts an array of hexadecimal chars into their binary equivalent
	 *
	 * @param hex string version of a hexadecimal char array
	 * @return a byte array representing the binary of the hexadecimal parameter
	 */
	public static byte[] convertHexToBytes(String hex) {
		return new BigInteger(hex, 16).toByteArray();
	}

}
