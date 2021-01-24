package uk.co.harieo.forceop.common.upgrade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ToJsonVersionUpgradeTask implements UpgradeTask {

	public static final String OLD_SERVER_FILE_NAME = "public_key";
	public static final String OLD_PROXY_FILE_NAME = "private_key";

	public static final String NEW_SERVER_FILE_NAME = "public_key.json";
	public static final String NEW_PROXY_FILE_NAME = "private_key.json";

	@Override
	public boolean needsUpgrade(Path baseDir) throws IOException {
		if (Files.exists(baseDir.resolve(OLD_SERVER_FILE_NAME)) && Files.exists(baseDir.resolve(OLD_PROXY_FILE_NAME))) {
			return !Files.exists(baseDir.resolve(NEW_SERVER_FILE_NAME)) || !Files.exists(baseDir.resolve(NEW_PROXY_FILE_NAME));
		}
		return false;
	}

	@Override
	public void upgrade(Path baseDir) throws IOException {
		System.out.println("Upgrade needed - upgrading to use JSON file storage");

		System.out.println(" ---- reading old server key");
		byte[] serverKey = Files.readAllBytes(baseDir.resolve(OLD_SERVER_FILE_NAME));

	}
}
