
package ru.spbau.svidchenko.asteroids_project.agentmodel;

import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

import java.io.Serializable;

public abstract class GunnerAgent implements Serializable {
    private boolean learningDisabled = false;

    public abstract String getName();
    public abstract GunnerPlayer buildPlayer(long id);

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
