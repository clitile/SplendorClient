package nz.ai;

import nz.ai.Context;
import nz.ai.Event;
import nz.ai.State;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

import java.text.MessageFormat;

public class StateMachine extends AbstractStateMachine<StateMachine, State, Event, Context> {

    private void getThreeCoin(State from, State to, Event event, Context player) {
        System.out.println("getThreeCoin");
    }

    private void getTwoSameCoin(State from, State to, Event event, Context player) {
        System.out.println("getTwoSameCoin");
    }

    private void getOneMidCard(State from, State to, Event event, Context player) {
        System.out.println("getOneMidCard");
    }

    private void getOneSaveCard(State from, State to, Event event, Context player) {
        System.out.println("getOneSaveCard");
    }
    private void getSaveCard(State from, State to, Event event, Context player) {
        System.out.println("getSaveCard");
    }
    private void toNotAction(State from, State to, Event event, Context player) {
        System.out.println("toNotAction");
    }
}

