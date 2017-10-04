package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public class LeftRollNFlyPilotAgent extends PilotAgent {
    @Override
    public String getName() {
        return "Mr. Left Roll";
    }

    @Override
    public PilotPlayer buildPlayer(long id) {
        return new PilotPlayer(id) {
            @Override
            protected Action chooseAction() {
                Action action = new Action();
                action.setMove(Action.Move.FORWARD);
                action.setTurn(Action.Turn.LEFT);
                return action;
            }
        };
    }
}
