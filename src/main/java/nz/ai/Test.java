package nz.ai;

import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;

public class Test {

    public static void main(String[] args) {

        StateMachineBuilder<StateMachine, State, Event, Context> builder =
                StateMachineBuilderFactory.create(StateMachine.class, State.class, Event.class, Context.class);
        /**
         * 状态转移表
         */
        builder.externalTransition().from(State.NotTurn).to(State.YourTurn).on(Event.ThreeCoin).callMethod("getThreeCoin");

        builder.externalTransition().from(State.NotTurn).to(State.YourTurn).on(Event.TwoCoin).callMethod("getTwoSameCoin");

        builder.externalTransition().from(State.NotTurn).to(State.YourTurn).on(Event.MidCard).callMethod("getOneMidCard");

        builder.externalTransition().from(State.NotTurn).to(State.YourTurn).on(Event.DevCard).callMethod("getOneSaveCard");

        builder.externalTransition().from(State.NotTurn).to(State.YourTurn).on(Event.ResCard).callMethod("getSaveCard");

        builder.externalTransition().from(State.YourTurn).to(State.NotTurn).on(Event.ToNot).callMethod("toNotAction");
        StateMachine machine = builder.newStateMachine(State.NotTurn);


//        Context patchouli = new Context("patchouli");
//        Context xiaoye = new Context("咲夜");
//
//        machine.start();
//
//        //patchouli审批拒绝
//        machine.fire(Event.APPROVE_REFUSED,patchouli);
//        machine.fire(Event.APPROVE_REFUSED,patchouli);
//
//        //xiaoye复核成功
//        machine.fire(Event.RECHECK_PASS,xiaoye);


    }

}
