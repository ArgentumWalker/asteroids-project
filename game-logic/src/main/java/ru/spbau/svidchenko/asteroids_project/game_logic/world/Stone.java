package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;

public class Stone extends Entity {
    public Stone(Point position, Point velocity) {
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
        private double angle = RandomGod.ask.nextDouble();
        private double dangle = (0.5 - RandomGod.ask.nextDouble()) * 2 * Constants.STONE_MAX_ANGLE_DELTA;
        private double orientation = angle;

        public Relative(double angle, Point center) {
            super(angle, center, Stone.this);
        }

        public Relative(Callable<Double> angleFunction, Callable<Point> centerFunction) {
            super(angleFunction, centerFunction, Stone.this);
        }

        public double getOrientation() {
            return orientation;
        }

        @Override
        protected void refresh(double angle, Point center) {
            super.refresh(angle, center);
            this.angle += dangle;
            orientation = this.angle - angle / 2;
        }
    }
}
