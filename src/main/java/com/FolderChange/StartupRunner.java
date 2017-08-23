package com.FolderChange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

	@Override
	public void run(String... args) throws Exception {
	}

	/*@Scheduled(cron = "0 0 10 * * SUN-FRI", zone = "Asia/Jerusalem")
	public void run() {

	}*/

}