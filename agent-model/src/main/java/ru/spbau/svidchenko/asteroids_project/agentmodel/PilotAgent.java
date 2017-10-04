package ru.spbau.svidchenko.asteroids_project.agentmodel;

import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.io.Serializable;

public abstract class PilotAgent implements Serializable {
    public abstract String getName();
    public abstract PilotPlayer buildPlayer(long id);
}
