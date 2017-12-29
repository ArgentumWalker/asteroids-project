package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GraphicStyleContainer {
    private Image vehicleImage = getVehicleImage();
    private Image weaponImage = getWeaponImage();
    private Image stoneImage = getStoneImage();
    private Image bulletImage = getBulletImage();
    private Image backgroundImage = getBackgroundImage();

    public GraphicStyleContainer() {}

    ////GAME CONSTANTS

    //Field & entities
    public abstract BlendMode getGameBlendModel();
    public abstract Effect getGameEffect();
    public abstract Paint getGridColor();

    //Ui
    public abstract BlendMode getUiBlendModel();
    public abstract Effect getUiEffect();
    public abstract TextStyle getTextStyle();

    //// MENU CONSTANTS
    public abstract TextStyle getMenuTitleTextStyle();
    public abstract TextStyle getMenuButtonTextStyle(int offset);
    public abstract TextStyle getMenuActiveTextStyle();

    //Game visual
    protected abstract Image getVehicleImage();
    protected abstract Image getWeaponImage();
    protected abstract Image getStoneImage();
    protected abstract Image getBulletImage();

    protected abstract Image getBackgroundImage();

    protected abstract Animation getShipAppearAnimation();
    protected abstract Animation getShipDieAnimation();
    protected abstract Animation getStoneAppearAnimation();
    protected abstract Animation getStoneDieAnimation();
    protected abstract Animation getBulletAppearAnimation();
    protected abstract Animation getBulletDieAnimation();

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

    public Image getBackground() {
        return backgroundImage;
    }

    public Animation getAnimationFor(WorldModel.Event event) {
        if (event.entity instanceof Ship) {
            switch (event.type) {
                case APPEAR:
                    return getShipAppearAnimation();
                case DIE:
                    return getShipDieAnimation();
            }
        }
        if (event.entity instanceof Stone) {
            switch (event.type) {
                case APPEAR:
                    return getStoneAppearAnimation();
                case DIE:
                    return getStoneDieAnimation();
            }
        }
        if (event.entity instanceof Bullet) {
            switch (event.type) {
                case APPEAR:
                    return getBulletAppearAnimation();
                case DIE:
                    return getBulletDieAnimation();
            }
        }
        return null;
    }

    private Point calculateWindowPosition(Point worldPosition) {
        return Point.with(Constants.WINDOW_HALF_WIDTH_PX / Constants.PIXELS_IN_WORLD_POINT,
                Constants.WINDOW_HALF_HEIGHT_PX / Constants.PIXELS_IN_WORLD_POINT)
                .add(worldPosition).mult(Constants.PIXELS_IN_WORLD_POINT);
    }

    public enum TextType {
        Stroke,
        Fill,
        StrokeAndFill
    }

    public enum Align {
        Left,
        Center,
        Right
    }


    public static class TextStyle {
        public final BlendMode blendMode;
        public final Effect effect;
        public final Font font;
        public final Align align;
        public final double strokeWidth;
        public final Paint strokePaint;
        public final Paint fillPaint;
        public final boolean isStroke;
        public final boolean isFill;

        public TextStyle(
                BlendMode blendMode,
                Effect effect,
                Font font,
                Align align,
                double strokeWidth,
                Paint strokePaint,
                Paint fillPaint
        ) {
            this.blendMode = blendMode;
            this.effect = effect;
            this.font = font;
            this.align = align;
            this.strokeWidth = strokeWidth;
            this.strokePaint = strokePaint;
            this.fillPaint = fillPaint;
            this.isStroke = true;
            this.isFill = true;
        }

        public TextStyle(BlendMode blendMode, Effect effect, Font font, Align align, Paint fillPaint) {
            this.blendMode = blendMode;
            this.effect = effect;
            this.font = font;
            this.align = align;
            this.fillPaint = fillPaint;
            this.isStroke = false;
            this.isFill = true;
            strokeWidth = 0;
            strokePaint = null;
        }

        public TextStyle(BlendMode blendMode, Effect effect, Font font, Align align, double strokeWidth, Paint strokePaint) {
            this.blendMode = blendMode;
            this.effect = effect;
            this.font = font;
            this.align = align;
            this.strokeWidth = strokeWidth;
            this.strokePaint = strokePaint;
            this.isStroke = true;
            this.isFill = false;
            this.fillPaint = null;
        }
    }
}
