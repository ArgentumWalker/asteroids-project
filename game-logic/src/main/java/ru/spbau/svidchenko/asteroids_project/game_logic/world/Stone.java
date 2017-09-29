package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Stone extends Entity {
    public Stone(@NotNull Point position, @NotNull Point velocity) {
        super(position, velocity.checkVelocitySize(Constants.STONE_MAX_VELOCITY),
                Constants.STONE_START_HEALTH, Constants.STONE_RADIUS);
    }
}
