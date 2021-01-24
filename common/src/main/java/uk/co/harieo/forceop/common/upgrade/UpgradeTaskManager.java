package uk.co.harieo.forceop.common.upgrade;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class UpgradeTaskManager {

	public static void checkAndRunUpgradeTasks(Path baseDir) {
		List<UpgradeTask> upgradeTasks = Arrays.asList(
				new ToJsonVersionUpgradeTask()
		);

		for (UpgradeTask task : upgradeTasks) {
			if (task.needsUpgrade(baseDir)) {
				task.upgrade(baseDir);
			}
		}
	}

}
