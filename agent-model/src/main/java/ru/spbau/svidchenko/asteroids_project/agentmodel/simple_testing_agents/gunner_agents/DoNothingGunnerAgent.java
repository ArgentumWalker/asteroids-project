package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public class DoNothingGunnerAgent extends GunnerAgent {
    @Override
    public String getName() {
        return "DoNothing";
    }

    @Override
    public GunnerPlayer buildPlayer(long id) {
        return new GunnerPlayer(id) {
            @Override
            protected Action chooseAction() {
                return new Action();
            }
        };
    }
}
