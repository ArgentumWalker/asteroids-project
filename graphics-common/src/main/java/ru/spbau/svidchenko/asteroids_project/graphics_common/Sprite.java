package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

public class Sprite {
    private Image image;
    private double rotation;
    private Point position;

    public Sprite(Image image, double rotation, Point position) {
        this.image = image;
        this.rotation = rotation;
        this.position = position;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setPosition(Point position) {
        this.position = position;
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
}
