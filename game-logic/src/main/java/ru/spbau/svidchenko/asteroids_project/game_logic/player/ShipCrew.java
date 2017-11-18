package ru.spbau.svidchenko.asteroids_project.game_logic.player;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;

import java.util.concurrent.TimeUnit;

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

    public void addDelayedScore(long score, long delay) {
        addScore(score);
        members.first().incScore(score, delay);
        members.second().incScore(score, delay);
    }

    public void addScore(long score) {
        this.score += score;
    }

    public long getLeftTimeSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(
                (Constants.TURNS_IN_GAME - members.first().vehicle.getShip().getLiveTime()) * Constants.MILLIS_PER_TURN);
    }
}
