package ru.spbau.svidchenko.asteroids_project.game_logic;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Entity;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.WorldModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private WorldModel currentWorldModel;
    private RandomGod random = new RandomGod();

    //CONSTRUCTORS

    public Game(WorldModel startPosition, WorldDescriptor worldDescriptor) {
        currentWorldModel = startPosition;
        currentWorldModel.removeEntities(currentWorldModel.getTypedEntities(Stone.class)
                .stream()
                .skip(worldDescriptor.stonesCount)
                .collect(Collectors.toList()));
    }

    public Game(WorldDescriptor worldDescriptor) {
        List<Stone> stones = new ArrayList<>();
        for (long i = 0; i < worldDescriptor.stonesCount; i++) {
            stones.add(new Stone(random.randomWorldPoint(), random.randomPoint(0, Constants.STONE_MAX_VELOCITY)));
        }
        currentWorldModel.addEntities(stones);
        List<Ship> ships = new ArrayList<>();
        for (long i = 0; i < worldDescriptor.shipCount; i++) {
            ships.add(new Ship(random.randomWorldPoint(), i));
        }
    }

    //TURN LOGIC

    public void nextTurn() {
        List<Entity> entities = currentWorldModel.getEntities();
        for (Entity entity : entities) {
            entity.move();
        }
        List<Entity> visitedEntities = new ArrayList<>();
        for (Entity entity1 : entities) {
            for (Entity entity2 : visitedEntities) {
                Point entity1Velocity = entity1.getVelocity();
                Point entity2Velocity = entity2.getVelocity();
                if (entity1.intersectsEntity(entity2)) {
                    entity2.receiveImpact(entity1Velocity, entity1.getPosition(),
                            entity1.physicalImpactsTo(entity2),
                            entity1.harmfulImpactsTo(entity2));
                }
                if (entity2.intersectsEntity(entity1)) {
                    entity1.receiveImpact(entity2Velocity, entity2.getPosition(),
                            entity2.physicalImpactsTo(entity1),
                            entity2.harmfulImpactsTo(entity1));
                }
            }
            visitedEntities.add(entity1);
        }
        List<Entity> entitiesToRemove = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isDead()) {
                if (entity instanceof Ship) {
                    entity.setPosition(random.randomWorldPoint());
                    entity.setVelocity(Point.with(0, 0));
                    entity.setHealth(Constants.SHIP_START_HEALTH);
                    continue;
                }
                if (entity instanceof Stone) {
                    entity.setPosition(random.randomWorldPoint());
                    entity.setVelocity(random.randomPoint(0, Constants.STONE_MAX_VELOCITY));
                    entity.setHealth(Constants.STONE_START_HEALTH);
                    continue;
                }
                entitiesToRemove.add(entity);
            }
        }
        entities.removeAll(entitiesToRemove);
    }

    //OTHER METHODS

    public WorldModel getCurrentWorldodel() {
        return currentWorldModel;
    }
}
