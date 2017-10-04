package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public class RandomFlyPilotAgent extends PilotAgent {
    @Override
    public String getName() {
        return "God of Random";
    }

    @Override
    public PilotPlayer buildPlayer(long id) {
        return new PilotPlayer(id) {
            @Override
            protected Action chooseAction() {
                Action action = new Action();
                switch (RandomGod.ask.nextInt(3)) {
                    case 0: {
                        action.setTurn(Action.Turn.LEFT);
                        break;
                    }
                    case 1: {
                        action.setTurn(Action.Turn.RIGHT);
                        break;
                    }
                    default: {
                        action.setTurn(Action.Turn.NO_TURN);
                        break;
                    }
                }
                switch (RandomGod.ask.nextInt(3)) {
                    case 1: {
                        action.setMove(Action.Move.OFF);
                    }
                    default: {
                        action.setMove(Action.Move.FORWARD);
                    }
                }
                return action;
            }
        };
    }
}
