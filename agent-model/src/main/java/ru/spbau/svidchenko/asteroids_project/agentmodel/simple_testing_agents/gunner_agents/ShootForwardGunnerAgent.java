package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public class ShootForwardGunnerAgent extends GunnerAgent {
    @Override
    public String getName() {
        return "ForwardShoot";
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            @Override
            protected Action chooseAction() {
                Action action = new Action();
                action.setShoot(true);
                return action;
            }
        };
    }
}
