package ru.spbau.svidchenko.asteroids_project.graphics_common.styles;

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
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;


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
    public TextStyle getMenuButtonTextStyle() {
        return new TextStyle(
                BlendMode.LIGHTEN,
                new GaussianBlur(2.0),
                Font.font("Times New Roman", 35),
                Align.Left,
                2.0,
                Color.GREENYELLOW
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
                Color.LIMEGREEN
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
}
