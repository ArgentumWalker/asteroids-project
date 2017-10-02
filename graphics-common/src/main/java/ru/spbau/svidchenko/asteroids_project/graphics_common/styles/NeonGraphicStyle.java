package ru.spbau.svidchenko.asteroids_project.graphics_common.styles;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;


public class NeonGraphicStyle extends GraphicStyleContainer {

    @Override
    public BlendMode getBlendModel() {
        return BlendMode.LIGHTEN;
    }

    @Override
    public Effect getEffect() {
        return new Lighting();
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
