package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public class RandomShootGunnerAgent extends GunnerAgent {
    @Override
    public String getName() {
        return "GodOfRandom";
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            @Override
            public Action chooseAction() {
                Action action = new Action();
                action.setShoot(true);
                switch (RandomGod.ask.nextInt(3)) {
                    case 0: {
                        action.setTurn(Action.Turn.LEFT);
                    }
                    case 1: {
                        action.setTurn(Action.Turn.RIGHT);
                    }
                    default: {
                        action.setTurn(Action.Turn.NO_TURN);
                    }
                }
                return action;
            }
        };
    }
}
