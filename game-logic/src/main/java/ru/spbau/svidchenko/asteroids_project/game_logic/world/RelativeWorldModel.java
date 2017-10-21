package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class RelativeWorldModel {
    private ReadWriteLock usageLock = new ReentrantReadWriteLock();
    private Set<EntityRelative> relatives;
    private WorldModel worldModel;
    private Callable<Double> angleFunction;
    private Callable<Point> centerFunction;
    private Double currentAngle;

    public RelativeWorldModel(double angle, Point center, WorldModel worldModel) {
        this(() -> angle, () -> center, worldModel);
    }

    public RelativeWorldModel(Callable<Double> angleFunction, Callable<Point> centerFunction, WorldModel worldModel) {
        this.worldModel = worldModel;
        this.angleFunction = angleFunction;
        this.centerFunction = centerFunction;
        relatives = worldModel.getEntities().stream()
                .map(entity -> entity.getRelative(angleFunction, centerFunction))
                .collect(Collectors.toSet());
    }

    public Set<EntityRelative> getRelatives() {
        return relatives;
    }

    public Double getCurrentAngle() {
        return currentAngle;
    }

    public void refresh() {
        usageLock.writeLock().lock();
        currentAngle = angleFunction.call();
        relatives.forEach(EntityRelative::refresh);
        relatives.removeAll(
                relatives.stream().filter(relative -> !worldModel.getEntities().contains(relative.getEntity())).collect(Collectors.toList())
        );
        Set<Entity> entities = relatives.stream().map(EntityRelative::getEntity).collect(Collectors.toSet());
        relatives.addAll(
                worldModel.getEntities().stream()
                        .filter(entity -> !entities.contains(entity))
                        .map(entity -> entity.getRelative(angleFunction, centerFunction))
                        .collect(Collectors.toSet())
        );
        usageLock.writeLock().unlock();
    }

    public Lock readLock() {
        return usageLock.readLock();
    }
}
