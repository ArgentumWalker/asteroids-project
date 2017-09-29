package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Bullet extends Entity {
    public Bullet(@NotNull Point position, @NotNull Point velocity) {
        super(position, velocity, Constants.BULLET_HEALTH, Constants.BULLET_RADIUS);
        notPhysicalImpacter = true;
    }

    @Override
    public void move() {
        receiveDamage(Constants.BULLET_DAMAGE_PER_MOVE);
        super.move();
    }
}
