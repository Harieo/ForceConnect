package uk.co.harieo.forceop.common.upgrade;

import java.io.IOException;
import java.nio.file.Path;

public interface UpgradeTask {

	boolean needsUpgrade(Path baseDir) throws IOException;

	void upgrade(Path baseDir) throws IOException;

}
