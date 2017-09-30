package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import com.sun.istack.internal.Nullable;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Entity;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

import java.util.List;

public abstract class Player {
    protected long id;
    protected RelativeWorldModel worldModel;
    protected long score;

    public Player(long id) {
        this.id = id;
        score = 0;
    }

    @Nullable
    public abstract List<? extends Entity> makeAction();

    public void setRelativeWorldModel(RelativeWorldModel worldModel) {
        this.worldModel = worldModel;
    }

    public void incScore(long reward) {
        score += reward;
    }

    public long getScore() {
        return score;
    }
}
