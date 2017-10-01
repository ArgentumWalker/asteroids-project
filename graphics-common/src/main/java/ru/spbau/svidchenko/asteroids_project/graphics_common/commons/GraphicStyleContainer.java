package ru.spbau.svidchenko.asteroids_project.graphics_common.commons;

import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Bullet;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphicStyleContainer {
    private Image vehicleImage;
    private Image weaponImage;
    private Image stoneImage;
    private Image bulletImage;

    public GraphicStyleContainer() {
        Image vehicleFullsizeImage = getVehicleImage();
        Image weaponFullsizeImage = getWeaponImage();
        Image stoneFullsizeImage = getStoneImage();
        Image bulletFullsizeImage = getBulletImage();
        double vehicleScaleKoef = Constants.SHIP_RADIUS * 2 * Constants.PIXELS_IN_WORLD_POINT
                / Math.max(vehicleFullsizeImage.getWidth(), vehicleFullsizeImage.getHeight());
        double weaponScaleKoef = Constants.WEAPON_RADIUS * 2 * Constants.PIXELS_IN_WORLD_POINT
                / Math.max(weaponFullsizeImage.getWidth(), weaponFullsizeImage.getHeight());
        double stoneScaleKoef = Constants.STONE_RADIUS * 2 * Constants.PIXELS_IN_WORLD_POINT
                / Math.max(stoneFullsizeImage.getWidth(), stoneFullsizeImage.getHeight());
        double bulletScaleKoef = Constants.BULLET_RADIUS * 2 * Constants.PIXELS_IN_WORLD_POINT
                / Math.max(bulletFullsizeImage.getWidth(), bulletFullsizeImage.getHeight());
        vehicleImage = GraphicUtils.scaleImage(vehicleFullsizeImage, vehicleScaleKoef);
        weaponImage = GraphicUtils.scaleImage(weaponFullsizeImage, weaponScaleKoef);
        bulletImage = GraphicUtils.scaleImage(bulletFullsizeImage, bulletScaleKoef);
        stoneImage = GraphicUtils.scaleImage(stoneFullsizeImage, stoneScaleKoef);
    }

    public List<Sprite> getSpritesFor(EntityRelative relative) {
        List<Sprite> result = new ArrayList<>();
        if (relative instanceof Ship.Relative) {
            result.add(new Sprite(vehicleImage, ((Ship.Relative) relative).getVehicleOrientation(), relative.getPosition()));
            result.add(new Sprite(weaponImage, ((Ship.Relative) relative).getWeaponOrientation(), relative.getPosition()));
        }
        if (relative instanceof Stone.Relative) {
            result.add(new Sprite(stoneImage, 0, relative.getPosition()));
        }
        if (relative instanceof Bullet.Relative) {
            result.add(new Sprite(bulletImage, 0, relative.getPosition()));
        }
        return result;
    }

    protected abstract Image getVehicleImage();
    protected abstract Image getWeaponImage();
    protected abstract Image getStoneImage();
    protected abstract Image getBulletImage();
}
