package ru.spbau.svidchenko.asteroids_project.commons;

public class Pair<N, M> {
    private N first;
    private M second;

    private Pair(N n, M m) {
        first = n;
        second = m;
    }

    public static <N, M> Pair<N, M> of(N first, M second) {
        return new Pair<>(first, second);
    }

    public N first() {
        return first;
    }

    public M second() {
        return second;
    }
}
