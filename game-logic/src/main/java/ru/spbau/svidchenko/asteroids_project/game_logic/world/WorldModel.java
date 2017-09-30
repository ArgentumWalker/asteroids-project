package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldModel {
    private Set<Entity> entities = new HashSet<>();

    public void addEntities(List<? extends Entity> addTarget) {
        entities.addAll(addTarget);
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public <T> Set<T> getTypedEntities(Class<T> clazz) {
        return entities.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toSet());
    }

    public void removeEntities(List<? extends Entity> removeTarget) {
        entities.removeAll(removeTarget);
    }
}
