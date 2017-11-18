package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Sprite {
    private Image image;
    private double rotation;
    private Point position;
    private Point size;

    public Sprite(Image image, double rotation, Point inWorldPosition, double entityRadius) {
        this.image = image;
        this.rotation = rotation;
        this.position = calculateCanvasPosition(inWorldPosition, entityRadius);
        this.size = calculateImageSize(entityRadius);
    }

    public Image getImage() {
        return image;
    }

    public double getRotation() {
        return rotation;
    }

    public Point getPosition() {
        return position;
    }

    public Point getSize() {
        return size;
    }

    private Point calculateCanvasPosition(Point worldPosition, double radius) {
        return Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                .add(Point.with(worldPosition.getY(), -worldPosition.getX())
                        .add(Point.with(-radius, -radius))
                        .mult(Constants.PIXELS_IN_WORLD_POINT));
    }

    private Point calculateImageSize(double entityRadius) {
        return Point.with(entityRadius * 2 * Constants.PIXELS_IN_WORLD_POINT, entityRadius * 2 * Constants.PIXELS_IN_WORLD_POINT);
    }
}
