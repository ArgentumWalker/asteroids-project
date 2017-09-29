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

    }

    //OTHER METHODS

    public WorldModel getCurrentWorldodel() {
        return currentWorldModel;
    }
}
