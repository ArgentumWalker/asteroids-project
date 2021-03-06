package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import ru.spbau.svidchenko.asteroids_project.game_logic.world.Entity;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

import java.util.List;

public abstract class Player {
    protected long id;
    protected RelativeWorldModel worldModel;

    public Player(long id) {
        this.id = id;
    }

    public abstract List<? extends Entity> makeAction();

    public void incScore(long reward) {}

    public RelativeWorldModel getWorldModel() {
        return worldModel;
    }

    public void setRelativeWorldModel(RelativeWorldModel worldModel) {
        this.worldModel = worldModel;
    }

}
