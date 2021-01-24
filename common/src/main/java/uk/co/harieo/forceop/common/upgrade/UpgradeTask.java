package uk.co.harieo.forceop.common.upgrade;

import java.nio.file.Path;

public interface UpgradeTask {

	boolean needsUpgrade(Path baseDir);

	void upgrade(Path baseDir);

}
