package uk.co.harieo.forceop.common.upgrade.v2_jsonfiles;

import uk.co.harieo.forceop.common.TokenFileHandler;
import uk.co.harieo.forceop.common.upgrade.UpgradeTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpgradeProxy implements UpgradeTask {

	public static final String OLD_SERVER_FILE_NAME = "public_key";
	public static final String OLD_PROXY_FILE_NAME = "private_key";

	@Override
	public boolean needsUpgrade(Path baseDir) throws IOException {
		if (Files.exists(baseDir.resolve(OLD_PROXY_FILE_NAME))) {
			return !Files.exists(baseDir.resolve(TokenFileHandlerV2.PROXY_KEY_FILE_NAME));
		}
		return false;
	}

	@Override
	public void upgrade(Path baseDir) throws IOException {
		System.out.println("Upgrade needed - upgrading to use JSON file storage");

		System.out.println(" ---- reading old server key");
		byte[] proxyKey = Files.readAllBytes(baseDir.resolve(OLD_PROXY_FILE_NAME));

		TokenFileHandlerV2 handler = new TokenFileHandlerV2(baseDir);
		handler.writeProxyKey(new String(proxyKey, TokenFileHandlerV2.ENCODING));
	}
}
