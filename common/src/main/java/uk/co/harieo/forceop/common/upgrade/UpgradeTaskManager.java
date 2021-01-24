package uk.co.harieo.forceop.common.upgrade;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class UpgradeTaskManager {

	public static void checkAndRunUpgradeTasks(Path baseDir) {
		List<UpgradeTask> upgradeTasks = Arrays.asList(
				new ToJsonVersionUpgradeTask()
		);

		try {
			for (UpgradeTask task : upgradeTasks) {
				if (task.needsUpgrade(baseDir)) {
					task.upgrade(baseDir);
				}
			}
		} catch (IOException ex) {
			System.out.println("Exception occurred while attempting upgrade tasks:");
			ex.printStackTrace();
		}
	}

}
