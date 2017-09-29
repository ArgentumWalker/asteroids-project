package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Ship extends Entity {
    protected long id;
    protected Weapon weapon;
    protected Vehicle vehicle;

    public Ship(@NotNull Point position, long id) {
        this(position, id, 0 ,0);
    }

    public Ship(@NotNull Point position, long id, long vehicleAngle, long weaponAngle) {
        super(position, Point.with(0, 0), Constants.SHIP_START_HEALTH, Constants.SHIP_RADIUS);
        weapon = new Weapon(weaponAngle);
        vehicle = new Vehicle(vehicleAngle);
        this.id = id;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public class Weapon {
        private long angle;

        Weapon(long angle) {
            this.angle = angle;
        }

        public void turnLeft() {
            angle++;
        }

        public void turnRight() {
            angle--;
        }

        public Bullet fire() {
            Point velocity = Point.withPolar(2 * Math.PI * angle/Constants.WEAPON_MOVES_TO_TURN,
                    Constants.BULLET_BASE_VELOCITY).add(getVelocity());
            return new Bullet(getPosition(), velocity);
        }

        public long getAngle() {
            return angle;
        }
    }

    public class Vehicle {
        private long angle;

        Vehicle(long angle) {
            this.angle = angle;
        }

        public void turnRight() {
            angle++;
        }

        public void turnLeft() {
            angle--;
        }

        public void moveForward() {
            velocity = Point.withPolar(2 * Math.PI * angle/Constants.WEAPON_MOVES_TO_TURN, Constants.SHIP_MAX_VELOCITY);
        }

        public void moveBackward() {
            velocity = Point.withPolar(-2 * Math.PI * angle/Constants.WEAPON_MOVES_TO_TURN, Constants.SHIP_MAX_VELOCITY);
        }

        public void stop() {
            velocity = Point.with(0, 0);
        }
    }
}
