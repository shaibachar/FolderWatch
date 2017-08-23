package com.FolderChange.statem.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.FolderChange.statem.Events;
import com.FolderChange.statem.States;

@Component
public class CreateActionImpl implements CreateAction {
	private static final Logger logger = LoggerFactory.getLogger(CreateActionImpl.class);

	@Autowired
	StateMachine<States, Events> stateMachine;

	@Override
	public void execute() {
		logger.info("execute Create");
		new Thread(new Runnable() {

			@Override
			public void run() {
				stateMachine.sendEvent(Events.DONE_CREATE);
			}
			
		}).start();
	}

}
