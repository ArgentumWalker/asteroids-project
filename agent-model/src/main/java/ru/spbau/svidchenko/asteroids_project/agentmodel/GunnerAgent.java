package ru.spbau.svidchenko.asteroids_project.agentmodel;

import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

import java.io.Serializable;

public abstract class GunnerAgent implements Serializable {
    public abstract String getName();
    public abstract GunnerPlayer buildPlayer(long id);
}
