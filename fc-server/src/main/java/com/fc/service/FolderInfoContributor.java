package com.fc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class FolderInfoContributor implements InfoContributor {

	@Autowired
	private FolderWatchService folderWatchService;

	@Override
	public void contribute(Info.Builder builder) {
		List<String> res = folderWatchService.getPaths().stream().map(x -> x.toString()).collect(Collectors.toList());
		builder.withDetail("registered folders", res);
	}

}