package uk.co.harieo.forceop.common;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DataConverterTest {

	private static class TestCase {
		private final String testName;
		private final byte[] bytes;
		private final String hex;

		private TestCase(String testName, byte[] bytes, String hex) {
			this.testName = testName;
			this.bytes = bytes;
			this.hex = hex;
		}
	}

	// table testing pattern: https://github.com/golang/go/wiki/TableDrivenTests
	List<TestCase> table;

	@Before
	public void initializeTable() {
		table = Arrays.asList(
				new TestCase("letters", genBytes(0xDE, 0xAD, 0xBE, 0xEF), "DEADBEEF"),
				new TestCase("numbers", genBytes(0x13, 0x37, 0xBE, 0xEF), "1337BEEF"),
				new TestCase("leading zeroes", genBytes(0, 1, 2, 3), "00010203"),
				new TestCase("empty string", new byte[0], ""),
				new TestCase(
						"longer input",
						genBytes(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
						"000102030405060708090A0B0C0D0E0F10"
				)
		);
	}

	@Test
	public void testConvertBytesToHex_doesNotMutate() {
		byte[] test = new byte[] { 0, 1, 2, 3, 4, 5 };
		byte[] copy = new byte[] { 0, 1, 2, 3, 4, 5 };

		DataConverter.convertBytesToHex(test);

		assertArrayEquals(copy, test);
	}

	@Test
	public void testConvertBytesToHex_table() {

		for (TestCase tc : table) {
			String result = DataConverter.convertBytesToHex(tc.bytes);
			assertEquals(tc.hex, result);
		}
	}

	@Test
	public void testConvertHexToBytes_table() {
		for (TestCase tc : table) {
			byte[] result = DataConverter.convertHexToBytes(tc.hex);
			assertArrayEquals(tc.bytes, result);
		}
	}

	@Test
	public void testConvertHexToBytes_lowercase() {
		String lowercase = "1337beef";

		byte[] result = DataConverter.convertHexToBytes(lowercase);

		assertArrayEquals(genBytes(0x13, 0x37, 0xBE, 0xEF), result);
	}

	@Test
	public void testSymmetrical() {
		for (TestCase tc : table) {
			byte[] bytes = DataConverter.convertHexToBytes(DataConverter.convertBytesToHex(tc.bytes));
			String hex = DataConverter.convertBytesToHex(DataConverter.convertHexToBytes(tc.hex));

			assertArrayEquals(tc.bytes, bytes);
			assertEquals(tc.hex, hex);
		}
	}

	private byte[] genBytes(int... ints) {
		byte[] bytes = new byte[ints.length];
		for (int i = 0; i < ints.length; i++) {
			bytes[i] = (byte) ints[i];
		}
		return bytes;
	}
}