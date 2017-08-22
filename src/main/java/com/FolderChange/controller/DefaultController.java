package com.FolderChange.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

	@GetMapping("/helloWrold")
	public ResponseEntity<String> helloWorld() throws IOException {

		return new ResponseEntity<>("Hello World!!", HttpStatus.OK);
	}
}
