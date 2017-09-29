package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Stone extends Entity {
    public Stone(@NotNull Point position, @NotNull Point velocity, long health, double radius) {
        super(position, velocity, health, radius);
    }
}
