package ru.spbau.svidchenko.asteroids_project.commons;

public class Point {
    private double x;
    private double y;

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point with(double x, double y) {
        return new Point(x, y);
    }

    public static Point withPolar(double angle, double size) {
        return new Point(size * Math.cos(angle), size * Math.sin(angle));
    }

    public double getAngle() {
        if (Math.abs(x) > Constants.EPS || Math.abs(y) > Constants.EPS) {
            return Math.atan2(y, x);
        }
        return 0;
    }

    public Point turn(double angle) {
        double oldX = x;
        double oldY = y;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        x = oldX * cos - oldY * sin;
        y = oldX * sin + oldY * cos;
        return this;
    }

    public Point add(Point p) {
        x += p.x;
        y += p.y;
        return this;
    }

    public Point checkWorldBounds() {
        if (x > Constants.WORLD_HALF_WIDTH) {
            x -= Constants.WORLD_HALF_WIDTH;
        }
        if (x < -Constants.WORLD_HALF_WIDTH) {
            x += Constants.WORLD_HALF_WIDTH;
        }
        if (y > Constants.WORLD_HALF_HEIGHT) {
            y -= Constants.WORLD_HALF_HEIGHT;
        }
        if (y < -Constants.WORLD_HALF_HEIGHT) {
            y += Constants.WORLD_HALF_HEIGHT;
        }
        return this;
    }

    public Point checkVelocitySize(double maxVelocity) {
        double currentSize = Math.sqrt(x*x + y*y);
        if (currentSize > maxVelocity) {
            x *= maxVelocity / currentSize;
            y *= maxVelocity / currentSize;
        }
        return this;
    }

    public double distanceTo(Point p) {
        double dx = Math.min(
                Math.min(
                        Math.abs(x - p.x),
                        Math.abs(x - p.x + Constants.WORLD_HALF_WIDTH * 2)),
                Math.abs(x - p.x - Constants.WORLD_HALF_WIDTH * 2));
        double dy = Math.min(
                Math.min(
                        Math.abs(y - p.y),
                        Math.abs(y - p.y + Constants.WORLD_HALF_HEIGHT * 2)),
                Math.abs(y - p.y - Constants.WORLD_HALF_HEIGHT * 2));
        return Math.sqrt(dx*dx + dy*dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
