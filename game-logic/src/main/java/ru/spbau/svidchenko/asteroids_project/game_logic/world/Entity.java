package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import com.sun.istack.internal.NotNull;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Entity {
    protected Point position;
    protected Point velocity;
    protected long health;
    protected double radius;

    protected Entity(@NotNull Point position, @NotNull Point velocity, long health, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.health = health;
    }

    public void move() {
        position.add(velocity);
    }

    public boolean receiveDamage(long damage) {
        health -= damage;
        return isDead();
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Point getPosition() {
        return position;
    }

    public Point getVelocity() {
        return velocity;
    }

    public Long getRemainingHealth() {
        return health;
    }

    public void setVelocity(Point newVelocity) {
        velocity = newVelocity;
    }

    public boolean intersectsEntity(Entity e) {
        return position.distanceTo(e.position) < e.radius + radius;
    }
}
