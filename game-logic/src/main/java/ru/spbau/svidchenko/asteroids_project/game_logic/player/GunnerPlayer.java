package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Bullet;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

public abstract class GunnerPlayer extends Player {
    private Ship.Weapon weapon;

    public GunnerPlayer(long id) {
        super(id);
    }

    protected abstract Action chooseAction();

    public final void setWeapon(Ship.Weapon weapon) {
        this.weapon = weapon;
    }

    public final @Nullable Bullet makeAction() {
        Action action = chooseAction();
        switch (action.turn) {
            case LEFT: {
                weapon.turnLeft();
                break;
            }
            case RIGHT: {
                weapon.turnRight();
                break;
            }
        }
        return action.shoot ? weapon.fire() : null;
    }

    protected final static class Action {
        public enum Turn {
            LEFT,
            RIGHT,
            NO_TURN
        }
        private Turn turn = Turn.NO_TURN;
        private boolean shoot = false;

        public void setTurn(@NotNull Turn turn) {
            this.turn = turn;
        }

        public void setShoot(boolean shoot) {
            this.shoot = shoot;
        }
    }
}
