package uk.co.harieo.forceop.common.upgrade;

import java.nio.file.Files;
import java.nio.file.Path;

public class ToJsonVersionUpgradeTask implements UpgradeTask {

	public static final String OLD_HASH_FILE_NAME = "public_key";
	public static final String OLD_TOKEN_FILE_NAME = "private_key";

	public static final String NEW_HASH_FILE_NAME = "public_key.json";
	public static final String NEW_TOKEN_FILE_NAME = "private_key.json";

	@Override
	public boolean needsUpgrade(Path baseDir) {
		if (Files.exists(baseDir.resolve(OLD_HASH_FILE_NAME)) && Files.exists(baseDir.resolve(OLD_TOKEN_FILE_NAME))) {
			return !Files.exists(baseDir.resolve(NEW_HASH_FILE_NAME)) || !Files.exists(baseDir.resolve(NEW_TOKEN_FILE_NAME));
		}
		return false;
	}

	@Override
	public void upgrade(Path baseDir) {
		System.out.println("Upgrade needed - upgrading to use JSON file storage");



	}
}
