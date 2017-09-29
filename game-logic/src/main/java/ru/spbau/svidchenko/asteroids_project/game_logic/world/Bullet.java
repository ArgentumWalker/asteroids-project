package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Bullet extends Entity {
    protected long parentShipId;

    public Bullet(@NotNull Point position, @NotNull Point velocity, long parentShipId) {
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
}
