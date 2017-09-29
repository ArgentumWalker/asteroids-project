package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Point;


public class EntityRelative<T extends Entity> {
    protected T entity;
    protected Point position;
    protected Callable<Double> angleFunction;
    protected Callable<Point> centerFunction;

    public EntityRelative(double angle, Point center, T entity) {
        angleFunction = () -> angle;
        Point centerClone = center.clone();
        centerFunction = () -> centerClone;
        this.entity = entity;
    }

    public EntityRelative(Callable<Double> angleFunction, Callable<Point> centerFunction, T entity) {
        this.angleFunction = angleFunction;
        this.centerFunction = centerFunction;
        this.entity = entity;
    }

    public void refresh() {
        refresh(angleFunction.call(), centerFunction.call());
    }

    protected void refresh(double angle, Point center) {
        position = entity.position.clone().add(center.getInverse()).turn(angle);
    }

    public Point getPosition() {
        return position;
    }

    public T getEntity() {
        return entity;
    }
}
