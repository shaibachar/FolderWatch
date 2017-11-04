package com.fc.a.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fc.a.statem.Events;
import com.fc.a.statem.States;

@Controller
public class DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	@Autowired
	StateMachine<States, Events> stateMachine;

	@PostMapping("/sendEvent/{event}")
	public ResponseEntity<String> sendEvent(@PathVariable("userId") String event){
		//TODO: fix this shit
		Events valueOf = Events.valueOf(event.toUpperCase());
		stateMachine.sendEvent(valueOf);
		return new ResponseEntity<String>(stateMachine.getState().getId().name(), HttpStatus.OK);
	}
	
	@PostMapping("/startStateMachine")
	public ResponseEntity<String> startStateMachine() {
		stateMachine.start();
		return new ResponseEntity<String>(stateMachine.getState().getId().name(), HttpStatus.OK);
	}
	
	@PostMapping("/stopStateMachine")
	public ResponseEntity<String> stopStateMachine() {
		stateMachine.stop();
		return new ResponseEntity<String>(stateMachine.getState().getId().name(), HttpStatus.OK);
	}

	@GetMapping("/getStateMachineState")
	public ResponseEntity<String> getStateMachineState() {
		return new ResponseEntity<String>(stateMachine.getState().getId().name(), HttpStatus.OK);
	}
	
}
