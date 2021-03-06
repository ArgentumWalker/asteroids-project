package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Bullet extends Entity {
    protected long parentShipId;

    public Bullet(Point position, Point velocity, long parentShipId) {
        super(position, velocity, Constants.BULLET_HEALTH, Constants.BULLET_RADIUS);
        this.parentShipId = parentShipId;
        notPhysicalImpacter = true;
    }

    @Override
    public void move() {
        receiveDamage(Constants.BULLET_DAMAGE_PER_MOVE);
        super.move();
    }

    @Override
    public boolean harmfulImpactsTo(Entity e) {
        if (e instanceof Ship) {
            if (((Ship) e).id == parentShipId) {
                return false;
            }
        }
        return super.harmfulImpactsTo(e);
    }

    public long getParentShipId() {
        return parentShipId;
    }

    @Override
    public EntityRelative getRelative(double angle, Point center) {
        return new Relative(angle, center);
    }

    @Override
    public EntityRelative getRelative(Callable<Double> angleFunction, Callable<Point> centerFunction) {
        return new Relative(angleFunction, centerFunction);
    }

    public class Relative extends EntityRelative<Bullet> {
        public Relative(double angle, Point center) {
            super(angle, center, Bullet.this);
        }

        public Relative(Callable<Double> angleFunction, Callable<Point> centerFunction) {
            super(angleFunction, centerFunction, Bullet.this);
        }
    }
}
