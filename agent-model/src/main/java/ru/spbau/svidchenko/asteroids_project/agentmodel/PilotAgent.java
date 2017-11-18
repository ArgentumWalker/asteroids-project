package ru.spbau.svidchenko.asteroids_project.agentmodel;

import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.io.Serializable;

public abstract class PilotAgent implements Serializable {
    private boolean learningDisabled = false;

    public abstract String getName();
    public abstract PilotPlayer buildPlayer(long id);

    protected final boolean isLearningDisabled() {
        return learningDisabled;
    }

    public final void disableLearning() {
        learningDisabled = true;
    }

    public final void enableLearning() {
        learningDisabled = false;
    }
}
