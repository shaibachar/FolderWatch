package com.FolderChange.service;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.FolderChange.statem.Events;
import com.FolderChange.statem.States;

@Component
public class FolderWatchServiceImpl implements FolderWatchService {
	private static final Logger logger = LoggerFactory.getLogger(FolderWatchServiceImpl.class);

	@Autowired
	StateMachine<States, Events> stateMachine;
	
	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final boolean recursive = true;
	private boolean watchLoop = true;
	private long watchLoopInterval = 1000;

	@SuppressWarnings("unchecked")
	private <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	public FolderWatchServiceImpl() throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
	}

	/**
	 * Register the given directory with the WatchService
	 * 
	 * @param path
	 * @throws IOException
	 */
	@Override
	public void register(Path path) throws IOException {
		WatchKey key = path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		Path prev = keys.get(key);
		if (prev == null) {
			logger.info(String.format("register: %s", path));
		} else {
			if (!path.equals(prev)) {
				logger.info(String.format("update: %s -> %s\n", prev, path));
			}
		}
		keys.put(key, path);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the WatchService.
	 * 
	 * @param startDir
	 * @throws IOException
	 */
	@Override
	public void registerAll(final Path startDir) throws IOException {
		logger.info("Scanning " + startDir + " ...");

		// register directory and sub-directories
		Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				logger.debug("Scanning " + dir + " ...");
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
		logger.info("Done register all path:" + startDir);
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	@Override
	public void startWatch() {
		Thread watch = new Thread(new Runnable() {

			@Override
			public void run() {
				while (watchLoop) {

					// wait for key to be signaled
					WatchKey key;
					try {
						key = watcher.take();
					} catch (InterruptedException x) {
						logger.debug("error while wait for a signal", x);
						return;
					}

					Path dir = keys.get(key);
					if (dir == null) {
						logger.error(String.format("WatchKey %s not recognized!!", key.toString()));
						continue;
					}

					processPolledEvents(key, dir);

					// reset key and remove from set if directory no longer accessible
					cleanupKeys(key);

					// all directories are inaccessible
					if (keys.isEmpty()) {
						logger.info("no directories are registered breaking the watchLoop");
						watchLoop = false;
						break;
					}
					try {
						Thread.sleep(watchLoopInterval);
					} catch (InterruptedException e) {
						logger.error("Error while looping the keys", e);
					}
				}

			}
		});
		watch.start();

	}

	@Override
	public void stopWatch() {
		watchLoop = false;
		logger.info("changed the loop flag to false");
	}

	private void cleanupKeys(WatchKey key) {
		boolean valid = key.reset();
		if (!valid) {
			keys.remove(key);

		}
	}

	private void processPolledEvents(WatchKey key, Path dir) {
		for (WatchEvent<?> event : key.pollEvents()) {
			WatchEvent.Kind kind = event.kind();

			if (kind == OVERFLOW) {
				logger.debug("lost or discarded event for key:" + key.toString());
				continue;
			}

			// Context for directory entry event is the file name of entry
			WatchEvent<Path> ev = cast(event);
			Path name = ev.context();
			Path child = dir.resolve(name);

			logger.info(String.format("%s: %s\n", event.kind().name(), child));
			stateMachine.sendEvent(Events.valueOf(event.kind().name()));

			// if directory is created, and watching recursively, then
			// register it and its sub-directories
			if (recursive && (kind == ENTRY_CREATE)) {
				try {
					if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
						registerAll(child);
					}
				} catch (IOException x) {
					logger.error("error while file or folder where created and system is trying to register", x);
				}
			}
		}
	}

	@Override
	public List<Path> getPaths() {
		return keys.values().stream().collect(Collectors.toList());
	}

	public boolean isWatchLoop() {
		return watchLoop;
	}

	public void setWatchLoop(boolean watchLoop) {
		this.watchLoop = watchLoop;
	}

	public long getWatchLoopInterval() {
		return watchLoopInterval;
	}

	public void setWatchLoopInterval(long watchLoopInterval) {
		this.watchLoopInterval = watchLoopInterval;
	}
}