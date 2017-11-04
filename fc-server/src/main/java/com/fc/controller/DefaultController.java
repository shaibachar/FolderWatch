package com.fc.controller;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fc.feign.StateMachineClient;
import com.fc.service.FolderWatchService;

@Controller
public class DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	@Autowired
	protected StateMachineClient remoteStateMachineClient;

	@Autowired
	private FolderWatchService folderWatchService;

	@GetMapping("/helloWrold")
	public ResponseEntity<String> helloWorld() throws IOException {

		return new ResponseEntity<>("Hello World!!", HttpStatus.OK);
	}

	@GetMapping("/getAllRegisteredFolders")
	public ResponseEntity<List<String>> getAllRegisteredFolders() {
		List<String> res = folderWatchService.getPaths().stream().map(x -> x.toString()).collect(Collectors.toList());
		if (res.size() > 0) {
			return new ResponseEntity<List<String>>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<String>>(new ArrayList<String>(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/registerFolder")
	public ResponseEntity<String> registerFoler(@RequestBody String path) {
		try {
			Path realPath = Paths.get(path);
			folderWatchService.registerAll(realPath);
		} catch (IOException e) {

		} catch (InvalidPathException x) {
			logger.error("Error while validate path", x);
			return new ResponseEntity<String>("invalid path:" + path, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("path:" + path + " was registered", HttpStatus.OK);
	}

	@PostMapping("/startStateMachine")
	public ResponseEntity<String> startStateMachine() {
		ResponseEntity<String> startStateMachine = remoteStateMachineClient.startStateMachine();
		return startStateMachine;
	}

	@PostMapping("/stopStateMachine")
	public ResponseEntity<String> stopStateMachine() {
		ResponseEntity<String> stopStateMachine = remoteStateMachineClient.stopStateMachine();
		return stopStateMachine;
	}

	@PostMapping("/getStateMachineState")
	public ResponseEntity<String> getStateMachineState() {
		ResponseEntity<String> stateMachineState = remoteStateMachineClient.getStateMachineState();
		return stateMachineState;
	}

}
