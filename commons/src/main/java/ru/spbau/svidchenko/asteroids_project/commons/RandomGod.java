package ru.spbau.svidchenko.asteroids_project.commons;

import java.util.List;
import java.util.Random;

public class RandomGod extends Random {
    public static final RandomGod ask = new RandomGod();

    public Point randomWorldPoint() {
        return Point.with((1 - nextDouble() * 2) * Constants.WORLD_HALF_WIDTH, (1 - nextDouble() * 2) * Constants.WORLD_HALF_HEIGHT);
    }

    public Point randomPoint(double minSize, double maxSize) {
        return Point.withPolar(2 * Math.PI * nextDouble(), minSize + (maxSize - minSize) * nextDouble());
    }

    public <T> T chooseRandom(List<T> variants) {
        return variants.get(nextInt(variants.size()));
    }
}
