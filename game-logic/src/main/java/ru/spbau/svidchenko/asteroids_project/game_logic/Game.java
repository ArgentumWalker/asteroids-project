package ru.spbau.svidchenko.asteroids_project.game_logic;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.Player;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.*;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private WorldModel currentWorldModel;
    private RandomGod random = RandomGod.ask;
    private List<RelativeWorldModel> relativeWorldModels = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private HashMap<Long, ShipCrew> shipId2shipCrew = new HashMap<>();
    private Set<Pair<Long, Entity>> reviveDelay = new HashSet<>();
    private Set<Player> frozenPlayers = new HashSet<>();

    //CONSTRUCTORS

    public Game(WorldDescriptor worldDescriptor) {
        //TODO check distances
        currentWorldModel = new WorldModel();
        List<Entity> entities = new ArrayList<>();
        for (long i = 0; i < Math.min(worldDescriptor.stonesCount, Constants.MAX_STONE_COUNT); i++) {
            entities.add(new Stone(random.randomWorldPoint(),
                    random.randomPoint(Constants.STONE_MIN_VELOCITY, Constants.STONE_MAX_VELOCITY)));
        }
        long shipId = 0;
        for (ShipCrew crew : worldDescriptor.players) {
            Ship ship = new Ship(random.randomWorldPoint(), shipId++);
            entities.add(ship);
            ship.setShipCrew(crew);
            crew.getMembers().first().setVehicle(ship.getVehicle());
            crew.getMembers().second().setWeapon(ship.getWeapon());
            RelativeWorldModel pilotWorldModel = new RelativeWorldModel(
                    () -> 2 * Math.PI * ship.getVehicle().getAngle() / Constants.VEHICLE_TURNS_TO_TURN_AROUND,
                    ship::getPosition, currentWorldModel);
            RelativeWorldModel gunnerWorldModel = new RelativeWorldModel(
                    () -> 2 * Math.PI * ship.getWeapon().getAngle() / Constants.WEAPON_TURNS_TO_TURN_AROUND,
                    ship::getPosition, currentWorldModel);
            crew.getMembers().first().setRelativeWorldModel(pilotWorldModel);
            crew.getMembers().second().setRelativeWorldModel(gunnerWorldModel);
            relativeWorldModels.add(pilotWorldModel);
            relativeWorldModels.add(gunnerWorldModel);
            players.add(crew.getMembers().first());
            players.add(crew.getMembers().second());
            shipId2shipCrew.put(ship.getId(), crew);
        }
        Set<Entity> spawned = new HashSet<>();
        for (Entity entity : entities) {
            respawn(entity, spawned);
            spawned.add(entity);
        }
        currentWorldModel.addEntities(entities);
        currentWorldModel.refreshTurn();
        afterTurn();
    }

    //TURN LOGIC

    public void nextTurn() {
        currentWorldModel.makeTurn();
        Set<Entity> entities = currentWorldModel.getEntities();
        for (Entity entity : entities) {
            entity.move();
        }
        processRevive();
        processImpacts(entities);
        processDying(entities);
        afterTurn();
    }

    private void processRevive() {
        Set<Pair<Long, Entity>> toReviveEntities = new HashSet<>();
        for (Pair<Long, Entity> queuedRevive : reviveDelay) {
            queuedRevive.setFirst(queuedRevive.first() - 1);
            if (queuedRevive.first() <= 0) {
                toReviveEntities.add(queuedRevive);
                if (queuedRevive.second() instanceof Ship) {
                    frozenPlayers.remove(((Ship) queuedRevive.second()).getShipCrew().getMembers().first());
                    frozenPlayers.remove(((Ship) queuedRevive.second()).getShipCrew().getMembers().second());
                }
            }
        }
        reviveDelay.removeAll(toReviveEntities);
        currentWorldModel.addEntities(toReviveEntities.stream().map(Pair::second).collect(Collectors.toList()));
    }

    private void processImpacts(Set<Entity> entities) {
        processImpactsSplit(entities, 0,
                Point.with(-Constants.WORLD_HALF_WIDTH, -Constants.WORLD_HALF_HEIGHT),
                Point.with(Constants.WORLD_HALF_WIDTH, Constants.WORLD_HALF_HEIGHT));
    }

    private void processImpactsSplit(Set<Entity> entities, long currentDepth, Point minBounds, Point maxBounds) {
        if (entities.size() < 2) {
            return;
        }
        if (currentDepth >= Constants.IMPACTS_CALCULATION_SPLIT_DEPTH || entities.size() < 8) {
            processImpactsDirectly(entities);
            return;
        }
        Set<Entity> upperLeft = new HashSet<>();
        Set<Entity> lowerLeft = new HashSet<>();
        Set<Entity> upperRight = new HashSet<>();
        Set<Entity> lowerRight = new HashSet<>();
        double middleX = (minBounds.getX() + maxBounds.getX()) / 2;
        double middleY = (minBounds.getY() + maxBounds.getY()) / 2;
        for (Entity entity : entities) {
            if (entity.getPosition()
                    .isLayInBoundsWithRadius(entity.getRadius(),
                            Point.with(middleX, middleY),
                            maxBounds)
            ) {
                upperRight.add(entity);
            }
            if (entity.getPosition()
                    .isLayInBoundsWithRadius(entity.getRadius(),
                            Point.with(minBounds.getX(), middleY),
                            Point.with(middleX, maxBounds.getY()))
                    ) {
                upperLeft.add(entity);
            }
            if (entity.getPosition()
                    .isLayInBoundsWithRadius(entity.getRadius(),
                            Point.with(middleX, minBounds.getY()),
                            Point.with(maxBounds.getX(), middleY))
                    ) {
                lowerRight.add(entity);
            }
            if (entity.getPosition()
                    .isLayInBoundsWithRadius(entity.getRadius(),
                            minBounds,
                            Point.with(middleX, middleY))
                    ) {
                lowerLeft.add(entity);
            }
        }
        processImpactsSplit(upperRight, currentDepth + 1,
                Point.with(middleX, middleY), maxBounds);
        processImpactsSplit(upperLeft, currentDepth + 1,
                Point.with(minBounds.getX(), middleY), Point.with(middleX, maxBounds.getY()));
        processImpactsSplit(lowerRight, currentDepth + 1,
                Point.with(middleX, minBounds.getY()), Point.with(maxBounds.getX(), middleY));
        processImpactsSplit(lowerLeft, currentDepth + 1,
                minBounds, Point.with(middleX, middleY));
    }

    private void processImpactsDirectly(Set<Entity> entities) {
        List<Entity> visitedEntities = new ArrayList<>();
        for (Entity entity1 : entities) {
            for (Entity entity2 : visitedEntities) {
                Point entity1Velocity = entity1.getVelocity();
                Point entity2Velocity = entity2.getVelocity();
                if (entity1.intersectsEntity(entity2)) {
                    entity2.receiveImpact(entity1Velocity, entity1.getPosition(),
                            !entity1.physicalImpactsTo(entity2),
                            !entity1.harmfulImpactsTo(entity2));
                    if (entity2.isDead()) {
                        processScore(entity2, entity1);
                    }
                }
                if (entity2.intersectsEntity(entity1)) {
                    entity1.receiveImpact(entity2Velocity, entity2.getPosition(),
                            !entity2.physicalImpactsTo(entity1),
                            !entity2.harmfulImpactsTo(entity1));
                    if (entity1.isDead()) {
                        processScore(entity1, entity2);
                    }
                }
            }
            visitedEntities.add(entity1);
        }
    }

    private void processDying(Set<Entity> entities) {
        List<Entity> entitiesToRemove = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.isDead()) {
                entitiesToRemove.add(entity);
            }
        }
        currentWorldModel.removeEntities(entitiesToRemove);
        for (Entity entity : entitiesToRemove) {
            if (entity instanceof Ship) {
                reviveDelay.add(Pair.of(Constants.SHIP_REVIVE_DELAY, entity));
                frozenPlayers.add(((Ship) entity).getShipCrew().getMembers().first());
                frozenPlayers.add(((Ship) entity).getShipCrew().getMembers().second());
                respawn(entity, entities);
                entity.setVelocity(Point.with(0, 0));
                entity.setHealth(Constants.SHIP_START_HEALTH);
            }
            if (entity instanceof Stone) {
                reviveDelay.add(Pair.of(Constants.STONE_REVIVE_DELAY, entity));
                respawn(entity, entities);
                entity.setVelocity(random.randomPoint(Constants.STONE_MIN_VELOCITY, Constants.STONE_MAX_VELOCITY));
                entity.setHealth(Constants.STONE_START_HEALTH);
            }
        }
        entitiesToRemove.forEach(currentWorldModel::addAppearEvent);
    }

    private void respawn(Entity entity, Set<Entity> entities) {
        boolean respawned = false;
        while (!respawned) {
            Point newPosition = random.randomWorldPoint();
            respawned = true;
            for (Entity checkEntity : entities) {
                respawned = respawned &&
                        checkEntity.getPosition().worldDistanceTo(newPosition) >
                                ((checkEntity instanceof Ship && entity instanceof Stone) ||
                                        (entity instanceof Ship && checkEntity instanceof Stone) ?
                                        Constants.SHIP_STONE_SPAWN_DISTANCE :
                                        Constants.SPAWN_DISTANCE_KOEF * (Math.max(checkEntity.getRadius(), entity.getRadius())));
            }
            for (Pair<Long, Entity> checkEntity : reviveDelay) {
                respawned = respawned &&
                        checkEntity.second().getPosition().worldDistanceTo(newPosition) >
                                ((checkEntity.second() instanceof Ship && entity instanceof Stone) ||
                                        (entity instanceof Ship && checkEntity.second() instanceof Stone) ?
                                        Constants.SHIP_STONE_SPAWN_DISTANCE :
                                        Constants.SPAWN_DISTANCE_KOEF * (Math.max(checkEntity.second().getRadius(), entity.getRadius())));
            }
            if (respawned) {
                entity.setPosition(newPosition);
            }
        }
    }

    private void afterTurn() {
        for (RelativeWorldModel worldModel : relativeWorldModels) {
            worldModel.refresh();
        }
        List<Entity> newEntities = new ArrayList<>();
        for (Player player : players) {
            if (!frozenPlayers.contains(player)) {
                List<? extends Entity> result = player.makeAction();
                if (result != null) {
                    newEntities.addAll(result);
                }
            }
        }
        currentWorldModel.addEntities(newEntities);
        newEntities.forEach(currentWorldModel::addAppearEvent);
    }

    private void processScore(Entity entity, Entity destroyedBy) {
        if (entity instanceof Ship) {
            shipId2shipCrew.get(((Ship) entity).getId()).addDelayedScore(Constants.SCORE_FOR_DEATH, 0);
        }
        if (destroyedBy instanceof Bullet){
            if (entity instanceof Ship) {
                shipId2shipCrew.get(((Bullet) destroyedBy).getParentShipId()).addDelayedScore(Constants.SCORE_FOR_DESTROY_SHIP,
                        Math.min(destroyedBy.getLiveTime() - 1, Constants.AGENT_LEARNING_MAX_DELAY));
            }
            if (entity instanceof Stone) {
                shipId2shipCrew.get(((Bullet) destroyedBy).getParentShipId()).addDelayedScore(Constants.SCORE_FOR_DESTROY_STONE,
                        Math.min(destroyedBy.getLiveTime() - 1, Constants.AGENT_LEARNING_MAX_DELAY));
            }
        }
    }

    //OTHER METHODS

    public WorldModel getCurrentWorldodel() {
        return currentWorldModel;
    }
}
