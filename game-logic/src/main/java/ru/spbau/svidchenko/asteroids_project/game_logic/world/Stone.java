package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Stone extends Entity {
    public Stone(@NotNull Point position, @NotNull Point velocity) {
        super(position, velocity.checkVelocitySize(Constants.STONE_MAX_VELOCITY),
                Constants.STONE_START_HEALTH, Constants.STONE_RADIUS);
    }

    @Override
    public EntityRelative getRelative(double angle, Point center) {
        return new Relative(angle, center);
    }

    @Override
    public EntityRelative getRelative(Callable<Double> angleFunction, Callable<Point> centerFunction) {
        return new Relative(angleFunction, centerFunction);
    }

    public class Relative extends EntityRelative<Stone> {
        public Relative(double angle, Point center) {
            super(angle, center, Stone.this);
        }

        public Relative(Callable<Double> angleFunction, Callable<Point> centerFunction) {
            super(angleFunction, centerFunction, Stone.this);
        }
    }
}
