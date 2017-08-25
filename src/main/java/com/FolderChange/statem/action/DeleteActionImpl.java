package com.FolderChange.statem.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.FolderChange.statem.Events;
import com.FolderChange.statem.States;

@Component
public class DeleteActionImpl implements DeleteAction {
	private static final Logger logger = LoggerFactory.getLogger(DeleteActionImpl.class);

	@Autowired
	StateMachine<States, Events> stateMachine;

	@Override
	public void execute() {
		logger.info("execute delete");
		new Thread(new Runnable() {

			@Override
			public void run() {
				stateMachine.sendEvent(Events.DONE_DELETE);
			}
			
		}).start();
	}

}
