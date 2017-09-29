package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldModel {
    private List<Entity> entities = new ArrayList<>();

    public void addEntities(List<? extends Entity> addTarget) {
        entities.addAll(addTarget);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public <T> List<T> getTypedEntities(Class<T> clazz) {
        return entities.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
    }

    public void removeEntities(List<? extends Entity> removeTarget) {
        entities.removeAll(removeTarget);
    }
}
