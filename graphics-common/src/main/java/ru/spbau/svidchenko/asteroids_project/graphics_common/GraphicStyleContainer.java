package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Bullet;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphicStyleContainer {
    private Image vehicleImage = getVehicleImage();
    private Image weaponImage = getWeaponImage();
    private Image stoneImage = getStoneImage();
    private Image bulletImage = getBulletImage();

    public GraphicStyleContainer() {}

    public abstract BlendMode getBlendModel();
    public abstract Effect getEffect();

    protected abstract Image getVehicleImage();
    protected abstract Image getWeaponImage();
    protected abstract Image getStoneImage();
    protected abstract Image getBulletImage();

    public List<Sprite> getSpritesFor(EntityRelative relative) {
        List<Sprite> result = new ArrayList<>();
        if (relative instanceof Ship.Relative) {
            result.add(new Sprite(vehicleImage, ((Ship.Relative) relative).getVehicleOrientation(),
                    relative.getPosition(), Constants.SHIP_RADIUS));
            result.add(new Sprite(weaponImage, ((Ship.Relative) relative).getWeaponOrientation(),
                    relative.getPosition(), Constants.WEAPON_RADIUS));
        }
        if (relative instanceof Stone.Relative) {
            result.add(new Sprite(stoneImage, ((Stone.Relative) relative).getOrientation(),
                    relative.getPosition(), Constants.STONE_RADIUS));
        }
        if (relative instanceof Bullet.Relative) {
            result.add(new Sprite(bulletImage, 0, relative.getPosition(), Constants.BULLET_RADIUS));
        }
        return result;
    }

    private Point calculateWindowPosition(Point worldPosition) {
        return Point.with(Constants.WINDOW_HALF_WIDTH_PX / Constants.PIXELS_IN_WORLD_POINT,
                Constants.WINDOW_HALF_HEIGHT_PX / Constants.PIXELS_IN_WORLD_POINT)
                .add(worldPosition).mult(Constants.PIXELS_IN_WORLD_POINT);
    }

}
