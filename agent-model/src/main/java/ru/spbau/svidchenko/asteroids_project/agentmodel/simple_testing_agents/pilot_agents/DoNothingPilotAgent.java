package ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public class DoNothingPilotAgent extends PilotAgent {
    @Override
    public String getName() {
        return "Mr. Do Nothing";
    }

    @Override
    public PilotPlayer buildPlayer(long id) {
        return new PilotPlayer(id) {
            @Override
            protected Action chooseAction() {
                return new Action();
            }
        };
    }
}
