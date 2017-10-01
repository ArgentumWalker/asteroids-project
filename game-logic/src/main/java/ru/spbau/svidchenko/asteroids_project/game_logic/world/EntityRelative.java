package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Point;


public class EntityRelative<T extends Entity> {
    protected T entity;
    protected Point position;
    protected Callable<Double> angleFunction;
    protected Callable<Point> centerFunction;

    public EntityRelative(double angle, Point center, T entity) {
        Point centerClone = center.clone();
        init(() -> angle, () -> centerClone, entity);
    }

    public EntityRelative(Callable<Double> angleFunction, Callable<Point> centerFunction, T entity) {
        init(angleFunction, centerFunction, entity);
    }

    private void init(Callable<Double> angleFunction, Callable<Point> centerFunction, T entity) {
        this.angleFunction = angleFunction;
        this.centerFunction = centerFunction;
        this.entity = entity;
        refresh();
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
