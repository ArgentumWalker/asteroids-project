package ru.spbau.svidchenko.asteroids_project.game_logic;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.Player;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    private WorldModel currentWorldModel;
    private RandomGod random = new RandomGod();
    private List<RelativeWorldModel> relativeWorldModels = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    //CONSTRUCTORS

    public Game(WorldDescriptor worldDescriptor) {
        currentWorldModel = new WorldModel();
        List<Stone> stones = new ArrayList<>();
        for (long i = 0; i < worldDescriptor.stonesCount; i++) {
            stones.add(new Stone(random.randomWorldPoint(), random.randomPoint(0, Constants.STONE_MAX_VELOCITY)));
        }
        currentWorldModel.addEntities(stones);
        List<Ship> ships = new ArrayList<>();
        long shipId = 0;
        for (Pair<PilotPlayer, GunnerPlayer> team : worldDescriptor.players) {
            Ship ship = new Ship(random.randomWorldPoint(), shipId++);
            ships.add(ship);
            team.first().setVehicle(ship.getVehicle());
            team.second().setWeapon(ship.getWeapon());
            RelativeWorldModel pilotWorldModel = new RelativeWorldModel(
                    () -> 2 * Math.PI * ship.getVehicle().getAngle() / Constants.VEHICLE_MOVES_TO_TURN,
                    ship::getPosition, currentWorldModel);
            RelativeWorldModel gunnerWorldModel = new RelativeWorldModel(
                    () -> 2 * Math.PI * ship.getWeapon().getAngle() / Constants.WEAPON_MOVES_TO_TURN,
                    ship::getPosition, currentWorldModel);
            team.first().setRelativeWorldModel(pilotWorldModel);
            team.second().setRelativeWorldModel(gunnerWorldModel);
            relativeWorldModels.add(pilotWorldModel);
            relativeWorldModels.add(gunnerWorldModel);
            players.add(team.first());
            players.add(team.second());
        }
        currentWorldModel.addEntities(ships);
        afterTurn();
    }

    //TURN LOGIC

    public void nextTurn() {
        Set<Entity> entities = currentWorldModel.getEntities();
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
        currentWorldModel.removeEntities(entitiesToRemove);
    }

    private void afterTurn() {
        for (RelativeWorldModel worldModel : relativeWorldModels) {
            worldModel.refresh();
        }
        List<Entity> newEntities = new ArrayList<>();
        for (Player player : players) {
            List<? extends Entity> result = player.makeAction();
            if (result != null) {
                newEntities.addAll(result);
            }
        }
        currentWorldModel.addEntities(newEntities);
    }

    //OTHER METHODS

    public WorldModel getCurrentWorldodel() {
        return currentWorldModel;
    }
}
