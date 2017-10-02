package ru.spbau.svidchenko.asteroids_project.game_logic;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;

import java.util.ArrayList;
import java.util.List;

public class WorldDescriptor {
    public long stonesCount = Constants.MAX_STONE_COUNT;
    public List<ShipCrew> players = new ArrayList<>();
}
