package com.fc.a.configuration;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.fc.a.statem.Events;
import com.fc.a.statem.States;
import com.fc.a.statem.action.CreateAction;
import com.fc.a.statem.action.DeleteAction;
import com.fc.a.statem.action.ModifiedAction;


@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {
	private static final Logger logger = LoggerFactory.getLogger(StateMachineConfig.class);

	@Autowired
	private CreateAction createAction;
	@Autowired
	private DeleteAction deleteAction;
	@Autowired
	private ModifiedAction modifiedAction;
	
	@Bean
	public Action<States, Events> createAction() {
		Action<States, Events> res = new Action<States, Events>() {
			
			@Override
			public void execute(StateContext<States, Events> ctx) {
				logger.info(ctx.getTarget().getId().toString()+" event: create");
				createAction.execute();
			}
		};
	    return res;
	}

	@Bean
	public Action<States, Events> modifyAction() {
		Action<States, Events> res = new Action<States, Events>() {
			
			@Override
			public void execute(StateContext<States, Events> ctx) {
				logger.info(ctx.getTarget().getId().toString()+" event: modify");
				modifiedAction.execute();
			}
		};
	    return res;
	}

	@Bean
	public Action<States, Events> deleteAction() {
	Action<States, Events> res = new Action<States, Events>() {
			
			@Override
			public void execute(StateContext<States, Events> ctx) {
				logger.info(ctx.getTarget().getId().toString()+" event: delete");
				deleteAction.execute();
			}
		};
	    return res;
	}

	
	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states.withStates().initial(States.START).states(EnumSet.allOf(States.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions
		.withExternal().source(States.START).target(States.PROCESS_CREATE).event(Events.ENTRY_CREATE).action(createAction())
		.and()
		.withExternal().source(States.START).target(States.PROCESS_MODIFY).event(Events.ENTRY_MODIFY).action(modifyAction())
		.and()
		.withExternal().source(States.START).target(States.PROCESS_DELETE).event(Events.ENTRY_DELETE).action(deleteAction())
		.and()
		.withExternal().source(States.PROCESS_CREATE).target(States.START).event(Events.DONE_CREATE)
		.and()
		.withExternal().source(States.PROCESS_MODIFY).target(States.START).event(Events.DONE_MODIFY)
		.and()
		.withExternal().source(States.PROCESS_DELETE).target(States.START).event(Events.DONE_DELETE);
	}
}