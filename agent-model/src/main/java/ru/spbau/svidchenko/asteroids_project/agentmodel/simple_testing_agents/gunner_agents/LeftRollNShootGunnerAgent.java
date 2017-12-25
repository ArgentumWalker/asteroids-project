package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public class LeftRollNShootGunnerAgent extends GunnerAgent {
    @Override
    public String getName() {
        return "LeftRoll";
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            @Override
            public Action chooseAction() {
                Action action = new Action();
                action.setShoot(true);
                action.setTurn(Action.Turn.LEFT);
                return action;
            }
        };
    }
}
