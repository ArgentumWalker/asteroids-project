package ru.spbau.svidchenko.asteroids_project.graphics_common.commons;

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

    public static Image scaleImage(Image source, double koef) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(source.getWidth() * koef);
        imageView.setFitHeight(source.getHeight() * koef);
        return imageView.snapshot(null, null);
    }

    public void drawImage(GraphicsContext context, Image source, Point position, double rotation) {
        context.save();
        Rotate rotate = new Rotate(rotation,
                position.getX() + source.getWidth() / 2,
                position.getY() + source.getHeight() / 2);
        context.setTransform(rotate.getMxx(), rotate.getMyx(),
                rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());
        context.drawImage(source, position.getX(), position.getY());
        context.restore();
    }

    public void drawSprite(GraphicsContext context, Sprite sprite) {
        drawImage(context, sprite.getImage(), sprite.getPosition(), sprite.getRotation());
    }

    public void drawWorld(GraphicsContext context, RelativeWorldModel relativeWorldModel, GraphicStyleContainer style) {
        context.clearRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        List<EntityRelative> visibleEntities = relativeWorldModel.getRelatives().stream().filter(relative ->
                (Math.abs(relative.getPosition().getX()) - relative.getEntity().getRadius()) * Constants.PIXELS_IN_WORLD_POINT
                        < Constants.WINDOW_HALF_WIDTH_Px &&
                (Math.abs(relative.getPosition().getY()) - relative.getEntity().getRadius()) * Constants.PIXELS_IN_WORLD_POINT
                                < Constants.WINDOW_HALF_WIDTH_Px
        ).collect(Collectors.toList());
        for (EntityRelative relative : visibleEntities) {
            for (Sprite sprite : style.getSpritesFor(relative)) {
                drawSprite(context, sprite);
            }
        }
    }
}
