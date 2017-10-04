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

    //GETTING NEW RELATIVE POINTS

    public Point clone() {
        return Point.with(x, y);
    }

    public Point getInverse() {
        return Point.with(-x, -y);
    }

    public Point getNormalized() {
        return Point.withPolar(getAngle(), 1.0);
    }

    public Point getProjection(Point line) {
        double lineAngle = line.getAngle();
        double cos = Math.cos(-lineAngle);
        double sin = Math.sin(-lineAngle);
        double tmp = x * cos - y * sin;
        return Point.with(tmp * cos, - tmp * sin);
    }

    public double getProjectionLength(Point line) {
        double lineAngle = line.getAngle();
        return x * Math.cos(-lineAngle) - y * Math.sin(-lineAngle);
    }

    //MODIFY POINT

    public Point rotate(double angle) {
        double oldX = x;
        double oldY = y;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        x = oldX * cos + oldY * sin;
        y = -oldX * sin + oldY * cos;
        return this;
    }

    public Point mult(double k) {
        x *= k;
        y *= k;
        return this;
    }

    public Point add(Point p) {
        x += p.x;
        y += p.y;
        return this;
    }

    public Point checkWorldBounds() {
        if (x > Constants.WORLD_HALF_WIDTH) {
            x -= Constants.WORLD_HALF_WIDTH * 2;
        }
        if (x < -Constants.WORLD_HALF_WIDTH) {
            x += Constants.WORLD_HALF_WIDTH * 2;
        }
        if (y > Constants.WORLD_HALF_HEIGHT) {
            y -= Constants.WORLD_HALF_HEIGHT * 2;
        }
        if (y < -Constants.WORLD_HALF_HEIGHT) {
            y += Constants.WORLD_HALF_HEIGHT * 2;
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

    //GET INFORMATION ABOUT POINT

    public double worldDistanceTo(Point p) {
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

    public Point worldDirectionTo(Point p) {
        double newX = p.x - x;
        if (newX < -Constants.WORLD_HALF_WIDTH) {
            newX += 2 * Constants.WORLD_HALF_WIDTH;
        }
        if (newX > Constants.WORLD_HALF_WIDTH) {
            newX -= 2 * Constants.WORLD_HALF_WIDTH;
        }
        double newY = p.y - y;
        if (newY < -Constants.WORLD_HALF_HEIGHT) {
            newY += 2 * Constants.WORLD_HALF_HEIGHT;
        }
        if (newY > Constants.WORLD_HALF_HEIGHT) {
            newY -= 2 * Constants.WORLD_HALF_HEIGHT;
        }
        return Point.with(newX, newY);
    }

    public double getAngle() {
        if (Math.abs(x) > Constants.EPS || Math.abs(y) > Constants.EPS) {
            return Math.atan2(y, x);
        }
        return 0;
    }

    public boolean isLayInBoundsWithRadius(double radius, Point minBounds, Point maxBounds) {
        double maxX = x + radius;
        double minX = x - radius;
        double maxY = y + radius;
        double minY = y - radius;
        double dx = 2 * Constants.WORLD_HALF_WIDTH;
        double dy = 2 * Constants.WORLD_HALF_HEIGHT;
        return  (__isLayInBoundsWithRadius(minX, maxX, minBounds.getX(), maxBounds.getX()) ||
                        __isLayInBoundsWithRadius(minX + dx, maxX + dx, minBounds.getX(), maxBounds.getX()) ||
                        __isLayInBoundsWithRadius(minX - dx, maxX - dx, minBounds.getX(), maxBounds.getX())) &&
                (__isLayInBoundsWithRadius(minY, maxY, minBounds.getY(), maxBounds.getY()) ||
                        __isLayInBoundsWithRadius(minY + dy, maxY + dy, minBounds.getY(), maxBounds.getY()) ||
                        __isLayInBoundsWithRadius(minY - dy, maxY - dy, minBounds.getY(), maxBounds.getY()));
    }

    private boolean __isLayInBoundsWithRadius(double a, double b, double boundA, double boundB) {
        return a < boundB && b > boundA;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
