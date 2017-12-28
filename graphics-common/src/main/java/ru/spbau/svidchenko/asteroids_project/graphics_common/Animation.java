package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.canvas.GraphicsContext;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public abstract class Animation{

    public abstract void draw(GraphicsContext context, Point worldPoint, double angle);
    public abstract long getLeftAnimationTurns();
    public abstract void passTurns(long turns);
}
