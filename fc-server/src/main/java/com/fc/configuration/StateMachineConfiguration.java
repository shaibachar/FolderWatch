package com.fc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("stateMachine")
public class StateMachineConfiguration {

	private String url;

}
