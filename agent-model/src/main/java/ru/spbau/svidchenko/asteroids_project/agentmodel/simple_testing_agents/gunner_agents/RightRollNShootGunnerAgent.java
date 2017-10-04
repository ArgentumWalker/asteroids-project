package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public class RightRollNShootGunnerAgent extends GunnerAgent {
    @Override
    public String getName() {
        return "RightRoll";
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            @Override
            protected Action chooseAction() {
                Action action = new Action();
                action.setShoot(true);
                action.setTurn(Action.Turn.RIGHT);
                return action;
            }
        };
    }
}
