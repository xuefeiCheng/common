package com.ebupt.portal.canyon.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 定时清理空的日志目录
 *
 * @author chy
 * @date 2019-03-01 14:33
 */
@Slf4j
@Component
public class LogSchedule {

	@Value("${logging.log-path:log}")
	private String logPath;

	private static final String LINUX_ABSOLUTE = "/";
	private static final String WINDOWS_ABSOLUTE = ":";

	@Scheduled(cron = "0 0 1 * * ?")
	public void clearEmptyLogDirectory() {
		if (StringUtils.isEmpty(logPath)) {
			log.error("未获取到日志路径，结束");
		} else {
			String realLogPath = logPath;
			if (!isAbsolutePath(logPath)) {
				String currentPath = System.getProperty("user.dir");
				realLogPath = currentPath + File.separator + logPath;
			}
			// 删除空白目录目录
			deleteEmptyLogDir(realLogPath + File.separator + "history-info");
			deleteEmptyLogDir(realLogPath + File.separator + "history-error");
		}
	}

	/**
	 * 判断是否为绝对路径
	 *
	 * @param path
	 *              路径
	 * @return
	 *              是/否
	 */
	private boolean isAbsolutePath(String path) {
		if (StringUtils.isNotEmpty(path)) {
			return path.startsWith(LINUX_ABSOLUTE) || path.indexOf(WINDOWS_ABSOLUTE) > 0;
		}
		return false;
	}

	/**
	 * 删除空的备份日志目录
	 *
	 * @param path
	 *              路径
	 */
	private void deleteEmptyLogDir(String path) {
		File bakDir = new File(path);
		if (bakDir.isDirectory()) {
			// 每天的备份目录
			File[] bakDateDirs = bakDir.listFiles();
			if (bakDateDirs != null) {
				for (File bakDateDir : bakDateDirs) {
					// 每天备份目录下的备份文件
					File[] bakFiles = bakDateDir.listFiles();
					// 备份文件已自动删除，手动删除空白目录
					if (bakFiles != null && bakFiles.length == 0) {
						bakDateDir.delete();
					}
				}
			}
		}
	}

}
