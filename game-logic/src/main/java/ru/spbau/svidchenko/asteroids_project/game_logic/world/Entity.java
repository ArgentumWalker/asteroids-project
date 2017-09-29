package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public abstract class Entity {
    protected Point position;
    protected Point velocity;
    protected long health;
    protected double radius;
    protected boolean ignorePhysicalImpact = false;
    protected boolean notPhysicalImpacter = false;

    protected Entity(@NotNull Point position, @NotNull Point velocity, long health, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.health = health;
    }

    //CHANGE STATE

    public void move() {
        position.add(velocity);
        position.checkWorldBounds();
    }

    public void receiveImpact(
            @NotNull Point impacterVelocity,
            @NotNull Point impacterPosition,
            boolean ignorePhysicalImpact,
            boolean ignoreHealthImpact
    ) {
        Point line = impacterPosition.getInverse().add(position);
        double impactSpeed = impacterVelocity.getProjectionLength(line) - velocity.getProjectionLength(line);

        if (impactSpeed > 0) {
            if (!ignoreHealthImpact) {
                receiveDamage((long) (impactSpeed * Constants.SPEED_TO_DAMAGE_KOEF));
            }
            if (!ignorePhysicalImpact) {
                Point velocityProjectionInv = velocity.getProjection(line).getInverse();
                Point impactVelocity = impacterVelocity.getProjection(line).add(velocityProjectionInv);
                if (notPhysicalImpacter) {
                    impactVelocity.mult(1/2);
                }
                velocity.add(velocityProjectionInv).add(impactVelocity);
            }
        }
    }

    public boolean receiveDamage(long damage) {
        health -= damage;
        return isDead();
    }

    //STATE GETTERS

    public boolean physicalImpactsTo(Entity e) {
        return !(notPhysicalImpacter || e.ignorePhysicalImpact);
    }

    public boolean harmfulImpactsTo(Entity e) {
        return true;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Point getPosition() {
        return position.clone();
    }

    public Point getVelocity() {
        return velocity.clone();
    }

    public Long getRemainingHealth() {
        return health;
    }

    public boolean intersectsEntity(Entity e) {
        return position.distanceTo(e.position) < e.radius + radius;
    }

    //FORCE ACTIONS

    public void setVelocity(Point newVelocity) {
        velocity = newVelocity;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setHealth(long health) {
        this.health = health;
    }

    //OTHER

    public abstract EntityRelative getRelative(double angle, Point center);

    public abstract EntityRelative getRelative(Callable<Double> angleFunction, Callable<Point> centerFunction);
}
