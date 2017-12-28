package ru.spbau.svidchenko.asteroids_project.graphics_common.styles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.graphics_common.Animation;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;

import java.util.List;


public class NeonGraphicStyle extends GraphicStyleContainer {

    @Override
    public BlendMode getGameBlendModel() {
        return BlendMode.LIGHTEN;
    }

    @Override
    public Effect getGameEffect() {
        return new GaussianBlur(1.0);
    }

    @Override
    public Paint getGridColor() {
        return Color.color(0, 1, 1, 0.2);
    }

    @Override
    public BlendMode getUiBlendModel() {
        return BlendMode.LIGHTEN;
    }

    @Override
    public Effect getUiEffect() {
        return new Lighting();
    }

    @Override
    public TextStyle getTextStyle() {
        return new TextStyle(
                BlendMode.LIGHTEN,
                new GaussianBlur(3.0),
                Font.font("Times New Roman", FontWeight.BOLD, 32),
                Align.Left,
                3.0,
                Color.LIMEGREEN
        );
    }

    @Override
    public TextStyle getMenuTitleTextStyle() {
        return new TextStyle(
                BlendMode.LIGHTEN,
                new GaussianBlur(5.0),
                Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 72),
                Align.Center,
                5.0,
                Color.YELLOW
        );
    }

    @Override
    public TextStyle getMenuButtonTextStyle(int offset) {
        return new TextStyle(
                BlendMode.LIGHTEN,
                new GaussianBlur(2.0),
                Font.font("Times New Roman", 35 * (Constants.AFTER_ACTIVE_BUTTONS_COUNT - (offset - 1)) / Constants.AFTER_ACTIVE_BUTTONS_COUNT),
                Align.Left,
                2.0,
                Color.LIME
        );
    }

    @Override
    public TextStyle getMenuActiveTextStyle() {
        return new TextStyle(
                BlendMode.LIGHTEN,
                new GaussianBlur(2.5),
                Font.font("Times New Roman", 42),
                Align.Left,
                2.5,
                Color.GREENYELLOW
        );
    }

    @Override
    protected Image getVehicleImage() {
        return new Image("/styles/neon/Vehicle.png");
    }

    @Override
    protected Image getWeaponImage() {
        return new Image("/styles/neon/Weapon.png");
    }

    @Override
    protected Image getStoneImage() {
        return new Image("/styles/neon/Stone.png");
    }

    @Override
    protected Image getBulletImage() {
        return new Image("/styles/neon/Bullet.png");
    }

    @Override
    protected Animation getShipAppearAnimation() {
        return new AppearAnimation(Constants.SHIP_REVIVE_DELAY, Constants.SHIP_REVIVE_DELAY * 2,
                Constants.SHIP_RADIUS * 2, Constants.SHIP_RADIUS * 3, Color.LIME);
    }

    @Override
    protected Animation getShipDieAnimation() {
        return new DieAnimation(15,
                Constants.SHIP_RADIUS * 0.85, Constants.SHIP_RADIUS * 2, Color.LIME);
    }

    @Override
    protected Animation getStoneAppearAnimation() {
        return new AppearAnimation(Constants.STONE_REVIVE_DELAY, Constants.STONE_REVIVE_DELAY,
                Constants.STONE_RADIUS * 1.2, Constants.STONE_RADIUS * 1.5, Color.YELLOW);
    }

    @Override
    protected Animation getStoneDieAnimation() {
        return new DieAnimation(15,
                Constants.STONE_RADIUS * 0.85, Constants.STONE_RADIUS * 1.5, Color.YELLOW);
    }

    @Override
    protected Animation getBulletAppearAnimation() {
        return new AppearAnimation(0, 5,
                0, Constants.BULLET_RADIUS * 2.5, Color.LIME);
    }

    @Override
    protected Animation getBulletDieAnimation() {
        return new DieAnimation(10,
                Constants.BULLET_RADIUS * 0.5, Constants.BULLET_RADIUS * 3.5, Color.LIME);
    }

    private class DieAnimation extends Animation {
        private long turnsLeft;
        private long turnsStart;
        private double startRadius;
        private double finishRadius;
        private Color color;
        //private List<Point> shards;

        public DieAnimation(long turns,
                            //int minShards, int maxShards,
                            double startRadius, double finishRadius,
                            Color color
        ) {
            turnsStart = turns;
            turnsLeft = turns;
            this.startRadius = startRadius;
            this.finishRadius = finishRadius;
            this.color = color;
        }

        @Override
        public void draw(GraphicsContext context, Point position, double angle) {
            if (turnsLeft < 0) {
                return;
            }
            double opacity = Math.min(1., Math.max(0., color.getOpacity() * Math.sqrt((1. * turnsLeft) / (turnsStart + 1))));
            double red = color.getRed();
            double green = color.getGreen();
            double blue = color.getBlue();
            double radius = finishRadius + (startRadius - finishRadius) * (turnsLeft) / (turnsStart + 1);
            context.setStroke(Color.color(red, green, blue, opacity));
            context.setLineWidth(5 * (turnsStart + 1) / ((turnsStart + 1) + 4 * (turnsLeft)));
            Point canvasPosition = calculateCanvasPosition(position, radius);
            context.strokeOval(canvasPosition.getX(), canvasPosition.getY(),
                    2*radius*Constants.PIXELS_IN_WORLD_POINT, 2*radius*Constants.PIXELS_IN_WORLD_POINT);
        }

        @Override
        public long getLeftAnimationTurns() {
            return turnsLeft;
        }

        @Override
        public void passTurns(long turns) {
            turnsLeft -= turns;
        }
    }

    private class AppearAnimation extends Animation {
        private long turnsLeft;
        private long middleTurns;
        private long turnsStart;
        private double startRadius;
        private double finishRadius;
        private Color color;
        //private List<Point> shards;

        public AppearAnimation(long startTurns, long middleTurns,
                            //int minShards, int maxShards,
                            double startRadius, double finishRadius,
                            Color color
        ) {
            turnsStart = startTurns;
            this.middleTurns = middleTurns;
            turnsLeft = startTurns + middleTurns;
            this.startRadius = startRadius;
            this.finishRadius = finishRadius;
            this.color = color;
        }

        @Override
        public void draw(GraphicsContext context, Point position, double angle) {
            if (turnsLeft < 0) {
                return;
            }
            double opacity;
            double red = color.getRed();
            double green = color.getGreen();
            double blue = color.getBlue();
            double radius;
            double width;
            if (turnsLeft > middleTurns) {
                opacity = Math.min(1., Math.max(0., color.getOpacity() * Math.sqrt(1. * (turnsStart + 2 + middleTurns - turnsLeft) / (turnsStart + 2))));
                radius = startRadius * (turnsLeft - middleTurns) / (turnsStart + 1);
                width = 5 * (turnsStart + 1) / ((turnsStart + 1) + 4 * (turnsStart + middleTurns - turnsLeft + 1));
            } else {
                opacity = Math.min(1., Math.max(0., color.getOpacity() * Math.sqrt((1. * turnsLeft) / (middleTurns + 1))));
                radius = finishRadius * (middleTurns - turnsLeft) / (middleTurns + 1);
                width = 5 * (middleTurns + 1) / ((middleTurns + 1) + 4 * (turnsLeft));
            }
            context.setStroke(Color.color(red, green, blue, opacity));
            context.setLineWidth(width);
            Point canvasPosition = calculateCanvasPosition(position, radius);
            context.strokeOval(canvasPosition.getX(), canvasPosition.getY(),
                    2*radius*Constants.PIXELS_IN_WORLD_POINT, 2*radius*Constants.PIXELS_IN_WORLD_POINT);
        }

        @Override
        public long getLeftAnimationTurns() {
            return turnsLeft;
        }

        @Override
        public void passTurns(long turns) {
            turnsLeft -= turns;
        }
    }

    private static Point calculateCanvasPosition(Point relativePosition, double radius) {
        return Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                .add(Point.with(relativePosition.getY(), -relativePosition.getX())
                        .add(Point.with(-radius, -radius))
                        .mult(Constants.PIXELS_IN_WORLD_POINT));
    }
}
