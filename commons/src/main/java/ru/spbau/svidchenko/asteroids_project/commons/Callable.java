package ru.spbau.svidchenko.asteroids_project.commons;

import java.io.Serializable;

public interface Callable<T> extends Serializable {
    T call();
}
