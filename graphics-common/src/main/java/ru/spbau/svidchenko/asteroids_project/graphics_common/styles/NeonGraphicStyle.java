package ru.spbau.svidchenko.asteroids_project.graphics_common.styles;

import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;


public class NeonGraphicStyle extends GraphicStyleContainer {

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
