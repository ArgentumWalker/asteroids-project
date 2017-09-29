package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.List;
import java.util.stream.Collectors;

public class RelativeWorldModel {
    private List<EntityRelative> relatives;

    public RelativeWorldModel(double angle, Point center, List<Entity> entities) {
        relatives = entities.stream().map(entity -> entity.getRelative(angle, center)).collect(Collectors.toList());
    }

    public RelativeWorldModel(Callable<Double> angleFunction, Callable<Point> centerFunction, List<Entity> entities) {
        relatives = entities.stream()
                .map(entity -> entity.getRelative(angleFunction, centerFunction))
                .collect(Collectors.toList());
    }

    public void refresh() {
        relatives.forEach(EntityRelative::refresh);
        relatives.removeAll(relatives.stream().filter(relative -> relative.entity.isDead()).collect(Collectors.toList()));
    }
}
