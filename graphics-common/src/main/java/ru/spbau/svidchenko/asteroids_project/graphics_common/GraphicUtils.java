package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;

import java.util.List;
import java.util.stream.Collectors;


public class GraphicUtils {

    public static void drawImage(GraphicsContext context, Image source, Point position, Point size, double rotation) {
        context.save();
        Rotate rotate = new Rotate(Math.toDegrees(rotation),
                position.getX() + size.getX() / 2,
                position.getY() + size.getY() / 2);
        context.setTransform(rotate.getMxx(), rotate.getMyx(),
                rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());
        context.drawImage(source, position.getX(), position.getY(), size.getX(), size.getY());
        context.restore();
    }

    public static void drawSprite(GraphicsContext context, Sprite sprite) {
        drawImage(context, sprite.getImage(), sprite.getPosition(), sprite.getSize(), sprite.getRotation());
    }

    public static void drawWorld(GraphicsContext context, RelativeWorldModel relativeWorldModel, GraphicStyleContainer style) {
        relativeWorldModel.readLock().lock();
        List<EntityRelative> visibleEntities = relativeWorldModel.getRelatives().stream().filter(relative ->
                (Math.abs(relative.getPosition().getX()) - relative.getEntity().getRadius()) * Constants.PIXELS_IN_WORLD_POINT
                        < Constants.WINDOW_HALF_WIDTH_PX &&
                (Math.abs(relative.getPosition().getY()) - relative.getEntity().getRadius()) * Constants.PIXELS_IN_WORLD_POINT
                                < Constants.WINDOW_HALF_HEIGHT_PX
        ).collect(Collectors.toList());
        for (EntityRelative relative : visibleEntities) {
            for (Sprite sprite : style.getSpritesFor(relative)) {
                drawSprite(context, sprite);
            }
        }
        relativeWorldModel.readLock().unlock();
    }
}
