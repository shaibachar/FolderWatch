package com.fc.a.statem.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.fc.a.statem.Events;
import com.fc.a.statem.States;

@Component
public class ModifiedActionImpl implements ModifiedAction{
	private static final Logger logger = LoggerFactory.getLogger(ModifiedActionImpl.class);

	@Autowired
	StateMachine<States, Events> stateMachine;

	@Override
	public void execute() {
		logger.info("execute Modified");
		new Thread(new Runnable() {

			@Override
			public void run() {
				stateMachine.sendEvent(Events.DONE_MODIFY);
			}
			
		}).start();
	}

}
