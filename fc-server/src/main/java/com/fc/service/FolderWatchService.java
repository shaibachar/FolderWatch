package com.fc.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FolderWatchService {

	void registerAll(Path startDir) throws IOException;

	void register(Path dir) throws IOException;

	void startWatch();

	void stopWatch();

	List<Path> getPaths();

}
