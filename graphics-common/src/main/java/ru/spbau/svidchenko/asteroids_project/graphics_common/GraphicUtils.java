package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.Menu;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.MenuButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class GraphicUtils {

    public static Point drawText(GraphicsContext context, Point position, String text, GraphicStyleContainer.TextStyle style) {
        if (style.isFill) {
            fillString(context, position, text, style.font, style.fillPaint, style.effect, style.blendMode);
        }
        if (style.isStroke) {
            strokeString(context, position, text, style.font, style.strokeWidth, style.strokePaint, style.effect, style.blendMode);
        }
        return getTextSizes(text, style);
    }

    public static void drawImageWithCenterRotation(GraphicsContext context, Image source, Point position, Point size, double rotation) {
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

    public static void drawImageWithCornerRotation(GraphicsContext context, Image source, Point position, Point size, double rotation) {
        context.save();
        Rotate rotate = new Rotate(Math.toDegrees(rotation),
                position.getX(),
                position.getY());
        context.setTransform(rotate.getMxx(), rotate.getMyx(),
                rotate.getMxy(), rotate.getMyy(),
                rotate.getTx(), rotate.getTy());
        context.drawImage(source, position.getX(), position.getY(), size.getX(), size.getY());
        context.restore();
    }

    public static void drawSprite(GraphicsContext context, Sprite sprite) {
        drawImageWithCenterRotation(context, sprite.getImage(), sprite.getPosition(), sprite.getSize(), sprite.getRotation());
    }

    public static void drawGameBackground(
            GraphicsContext context,
            RelativeWorldModel relativeWorldModel,
            GraphicStyleContainer style
    ) {
        double angle = relativeWorldModel.getCurrentAngle();
        Point center = relativeWorldModel.getCurrentCenter();
        center = Point.with(-center.getY(), center.getX());

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Point bgPos = Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX).add(
                        Point.with(- (2 * i + 1) * Constants.WORLD_HALF_WIDTH, - (2 * j + 1) * Constants.WORLD_HALF_HEIGHT)
                                .add(center)
                                .rotate(angle)
                                .mult(Constants.PIXELS_IN_WORLD_POINT)
                );
                drawImageWithCornerRotation(context, style.getBackground(), bgPos,
                        Point.with(Constants.WORLD_HALF_HEIGHT, Constants.WORLD_HALF_WIDTH).mult(2 * Constants.PIXELS_IN_WORLD_POINT),
                        -angle);
            }
        }

        context.save();
        context.setStroke(style.getGridColor());
        context.setLineWidth(4);
        //context.setGlobalBlendMode(style.getGameBlendModel());
        //context.setEffect(style.getGameEffect());
        for (int i = - 2 * Constants.GRID_DIVIDERS + 1; i < 2 * Constants.GRID_DIVIDERS; i++) {
            Point start = Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                    .add(Point.with(
                            i * Constants.WORLD_HALF_WIDTH / Constants.GRID_DIVIDERS,
                            - 2 * Constants.WORLD_HALF_HEIGHT)
                            .add(center)
                            .rotate(angle)
                            .mult(Constants.PIXELS_IN_WORLD_POINT));
            Point end = Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                    .add(Point.with(
                            i * Constants.WORLD_HALF_WIDTH / Constants.GRID_DIVIDERS,
                            2 * Constants.WORLD_HALF_HEIGHT)
                            .add(center)
                            .rotate(angle)
                            .mult(Constants.PIXELS_IN_WORLD_POINT));
            context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
            start = Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                    .add(Point.with(
                            - 2 * Constants.WORLD_HALF_WIDTH,
                            i * Constants.WORLD_HALF_HEIGHT / Constants.GRID_DIVIDERS)
                            .add(center)
                            .rotate(angle)
                            .mult(Constants.PIXELS_IN_WORLD_POINT));
            end = Point.with(Constants.WINDOW_HALF_WIDTH_PX, Constants.WINDOW_HALF_HEIGHT_PX)
                    .add(Point.with(
                            2 * Constants.WORLD_HALF_WIDTH,
                            i * Constants.WORLD_HALF_HEIGHT / Constants.GRID_DIVIDERS)
                            .add(center)
                            .rotate(angle)
                            .mult(Constants.PIXELS_IN_WORLD_POINT));
            context.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
        context.restore();
    }

    public static void drawWorld(
            GraphicsContext context,
            RelativeWorldModel relativeWorldModel,
            GraphicStyleContainer style,
            Set<Pair<Animation, Point>> animations
    ) {
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
        Point relativeVector = relativeWorldModel.getCurrentCenter().getInverse();
        double relativeAngle = relativeWorldModel.getCurrentAngle();
        relativeWorldModel.readLock().unlock();
        context.save();
        for (Pair<Animation, Point> animation : animations) {
            animation.first()
                    .draw(context,
                            animation.second().clone().add(relativeVector).rotate(relativeAngle),
                            relativeAngle);
        }
        context.restore();
    }

    public static void drawUi(
            GraphicsContext context,
            RelativeWorldModel relativeWorldModel,
            GraphicStyleContainer style,
            ShipCrew crew
    ) {
        GraphicStyleContainer.TextStyle textStyle = style.getTextStyle();
        String text = "Score: " + crew.getScore();
        double yOffset = 20 + getTextSizes(text, textStyle).getY();
        drawText(context, Point.with(20, yOffset), text, textStyle);
        long leftTime = crew.getLeftTimeSeconds();
        text = "Time left: " + leftTime / 60 + ":" + leftTime % 60;
        yOffset += 20 + getTextSizes(text, textStyle).getY();
        drawText(context, Point.with(20, yOffset), text, textStyle);
        //TODO: implement
    }

    public static void drawMenu(GraphicsContext context, Menu menu, GraphicStyleContainer style) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
        //Title
        GraphicStyleContainer.TextStyle textStyle = style.getMenuTitleTextStyle();
        Point canvasSize = Point.with(context.getCanvas().getWidth(), context.getCanvas().getHeight());
        double horizontalOffset = drawText(context,
                menuTextCalculatePosition(
                        menu.getTitle(), textStyle,
                        Point.with(Constants.MENU_TITLE_HORIZONTAL_INDENT_PX, Constants.MENU_TITLE_VERTICAL_INDENT_PX),
                        canvasSize
                ),
                menu.getTitle(), textStyle).getY();
        //Buttons
        int startIndex = Math.max(menu.getActiveButtonPosition() - Constants.AFTER_ACTIVE_BUTTONS_COUNT, 0);
        int endIndex = Math.min(menu.getActiveButtonPosition() + Constants.AFTER_ACTIVE_BUTTONS_COUNT,
                menu.getButtons().size() - 1);
        horizontalOffset += Constants.MENU_AFTER_TITLE_VERTICAL_INDENT_PX;
        for (int i = startIndex; i < endIndex + 1; i++) {
            MenuButton button = menu.getButtons().get(i);
            int offset = Math.abs(i - menu.getActiveButtonPosition());
            textStyle = menu.isActiveButton(button) ? style.getMenuActiveTextStyle() : style.getMenuButtonTextStyle(offset);
            horizontalOffset += drawText(context,
                    menuTextCalculatePosition(
                            button.getText(), textStyle,
                            Point.with(Constants.MENU_BUTTON_HORIZONTAL_INDENT_PX, horizontalOffset),
                            canvasSize
                    ),
                    button.getText(), textStyle).getY();
            horizontalOffset += Constants.MENU_BUTTON_VERTICAL_INDENT_PX;
        }
    }

    private static Point menuTextCalculatePosition(String text, GraphicStyleContainer.TextStyle textStyle, Point offsets, Point canvasSize) {
        Point sizes = getTextSizes(text, textStyle);
        switch (textStyle.align) {
            case Center: {
                return Point.with((canvasSize.getX() - sizes.getX()) / 2, offsets.getY() + sizes.getY());
            }
            case Right: {
                return Point.with(canvasSize.getX() - sizes.getX() - offsets.getX(), offsets.getY() + sizes.getY());
            }
            case Left:
            default: {
                return Point.with(offsets.getX(), offsets.getY() + sizes.getY());
            }
        }
    }

    private static Point getTextSizes(String text, GraphicStyleContainer.TextStyle style) {
        Text textE = new Text(text);
        textE.setFont(style.font);
        textE.setEffect(style.effect);
        textE.setBlendMode(style.blendMode);
        if (style.isStroke) {
            textE.setStrokeWidth(style.strokeWidth);
            textE.setStroke(style.strokePaint);
        }
        if (style.isFill) {
            textE.setFill(style.fillPaint);
        }
        return Point.with(textE.getLayoutBounds().getWidth(), textE.getLayoutBounds().getHeight());
    }

    private static void strokeString(
            GraphicsContext context,
            Point position,
            String text,
            Font font,
            double lineWidth,
            Paint strokeColor,
            Effect effect,
            BlendMode blendMode
    ) {
        context.save();
        context.setFont(font);
        context.setEffect(effect);
        context.setGlobalBlendMode(blendMode);
        context.setStroke(strokeColor);
        context.setLineWidth(lineWidth);
        context.strokeText(text, position.getX(), position.getY());
        context.restore();
    }

    private static void fillString(
            GraphicsContext context,
            Point position,
            String text,
            Font font,
            Paint fillColor,
            Effect effect,
            BlendMode blendMode
    ) {
        context.save();
        context.setFont(font);
        context.setEffect(effect);
        context.setGlobalBlendMode(blendMode);
        context.setFill(fillColor);
        context.strokeText(text, position.getX(), position.getY());
        context.restore();
    }
}
