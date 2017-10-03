package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

public class ShipCrew {
    private Pair<PilotPlayer, GunnerPlayer> members;
    private long score = 0;

    public ShipCrew(PilotPlayer pilot, GunnerPlayer gunner) {
        members = Pair.of(pilot, gunner);
    }

    public Pair<PilotPlayer, GunnerPlayer> getMembers() {
        return members;
    }

    public long getScore() {
        return score;
    }

    public void addScore(long score) {
        this.score += score;
        members.first().incScore(score);
        members.second().incScore(score);
    }
}
