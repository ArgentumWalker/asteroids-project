package ru.spbau.svidchenko.asteroids_project.game_logic.world;

import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldModel {
    private Set<Entity> entities = new HashSet<>();
    private Set<Event> currentEvents = new HashSet<>();
    private long turn = 0;

    public void addEntities(List<? extends Entity> addTarget) {
        entities.addAll(addTarget);
    }

    public void addAppearEvent(Entity appearedEntity) {
        currentEvents.add(new Event(EventType.APPEAR, appearedEntity, appearedEntity.position.clone(), turn));
    }

    public long getTurn() {
        return turn;
    }

    public Set<Event> getCurrentEvents() {
        return currentEvents;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public <T> Set<T> getTypedEntities(Class<T> clazz) {
        return entities.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toSet());
    }

    public void removeEntities(List<? extends Entity> removeTarget) {
        entities.removeAll(removeTarget);
        for (Entity entity : removeTarget) {
            currentEvents.add(new Event(EventType.DIE, entity, entity.position.clone(), turn));
        }
    }

    public void makeTurn() {
        turn++;
        currentEvents.clear();
    }

    public void refreshTurn() {
        turn = 0;
        currentEvents.clear();
    }

    public class Event {
        public final EventType type;
        public final Entity entity;
        public final Point position;
        public final long turn;

        public Event(EventType type, Entity entity, Point position, long turn) {
            this.type = type;
            this.entity = entity;
            this.position = position;
            this.turn = turn;
        }
    }

    public enum EventType {
        APPEAR,
        DIE
    }
}
