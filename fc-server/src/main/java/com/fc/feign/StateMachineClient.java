package com.fc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fca-microservice")
public interface StateMachineClient {

	@RequestMapping(method = RequestMethod.POST, value = "/sendEvent/{event}")
	public ResponseEntity<String> sendEvent(@PathVariable("userId") String event);

	@RequestMapping(method = RequestMethod.POST, value = "/startStateMachine")
	public ResponseEntity<String> startStateMachine();

	@RequestMapping(method = RequestMethod.POST, value = "/stopStateMachine")
	public ResponseEntity<String> stopStateMachine();

	@RequestMapping(method = RequestMethod.GET, value = "/getStateMachineState")
	public ResponseEntity<String> getStateMachineState();
}
