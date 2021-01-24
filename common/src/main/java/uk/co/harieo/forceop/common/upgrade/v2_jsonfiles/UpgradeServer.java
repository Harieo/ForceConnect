package uk.co.harieo.forceop.common.upgrade.v2_jsonfiles;

import uk.co.harieo.forceop.common.upgrade.UpgradeTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpgradeServer implements UpgradeTask {

	public static final String OLD_SERVER_FILE_NAME = "public_key";

	@Override
	public boolean needsUpgrade(Path baseDir) throws IOException {
		if (Files.exists(baseDir.resolve(OLD_SERVER_FILE_NAME))) {
			return !Files.exists(baseDir.resolve(TokenFileHandlerV2.SERVER_KEY_FILE_NAME));
		}
		return false;
	}

	@Override
	public void upgrade(Path baseDir) throws IOException {
		System.out.println("Upgrade needed - upgrading to use JSON file storage");

		System.out.println(" ---- reading old server key");
		byte[] serverKey = Files.readAllBytes(baseDir.resolve(OLD_SERVER_FILE_NAME));

		TokenFileHandlerV2 tokenFileHandler = new TokenFileHandlerV2(baseDir);
		tokenFileHandler.writeServerKey(serverKey);
	}
}
