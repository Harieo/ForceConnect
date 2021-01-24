package uk.co.harieo.forceop.common;

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
	 * @param hexChars an event number of hexadecimal chars
	 * @return a byte array representing the binary of the hexadecimal parameter
	 */
	public static byte[] convertHexToBytes(char[] hexChars) {
		byte[] bytes = new byte[hexChars.length / 2];

		int byteIndex = 0;
		for (int i = 0; i < hexChars.length; i+=2) {
			char firstChar = hexChars[i];
			char secondChar = hexChars[i + 1];

			bytes[byteIndex] = (byte) (decodeHexChar(firstChar)*16 + decodeHexChar(secondChar));
			byteIndex++;
		}

		return bytes;
	}

	/**
	 * An overload of {@link #convertHexToBytes(char[])} which accepts a String and converts it to a char array first
	 *
	 * @param hex string version of a hexadecimal char array
	 * @return a byte array representing the binary of the hexadecimal parameter
	 */
	public static byte[] convertHexToBytes(String hex) {
		return convertHexToBytes(hex.toCharArray());
	}

	/**
	 * Converts a hexadecimal character into a byte
	 *
	 * @param hexChar to convert to a byte
	 * @return the byte
	 */
	private static byte decodeHexChar(char hexChar) {
		hexChar = Character.toLowerCase(hexChar);
		if (hexChar >= 'a' && hexChar <= 'f') {
			return (byte) (hexChar - 'a' + 10);
		} else if (hexChar >= '0' && hexChar <= '9') {
			return (byte) (hexChar - '0');
		} else {
			throw new IllegalArgumentException("Invalid hexadecimal: " + hexChar);
		}
	}

}
