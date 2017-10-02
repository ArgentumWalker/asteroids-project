package ru.spbau.svidchenko.asteroids_project.graphics_common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.Menu;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.MenuButton;

import java.util.List;
import java.util.stream.Collectors;


public class GraphicUtils {

    public static Point drawText(GraphicsContext context, Point position, String text, GraphicStyleContainer.TextStyle style) {
        if (style.isFill) {
            fillString(context, position, text, style.font, style.fillPaint, style.effect, style.blendMode);
        }
        if (style.isStroke) {
            strokeString(context, position, text, style.font, style.strokeWidth, style.strokePaint, style.effect, style.blendMode);
        }
        return getTextSizes(text, style).add(position);
    }

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

    public static void drawMenu(GraphicsContext context, Menu menu, GraphicStyleContainer style) {
        GraphicStyleContainer.TextStyle textStyle = style.getMenuTitleTextStyle();
        Point canvasSize = Point.with(context.getCanvas().getWidth(), context.getCanvas().getHeight());
        double horizontalOffset = drawText(context,
                menuTextCalculatePosition(
                        menu.getTitle(), textStyle,
                        Point.with(Constants.MENU_TITLE_HORIZONTAL_INDENT_PX, Constants.MENU_TITLE_VERTICAL_INDENT_PX),
                        canvasSize
                ),
                menu.getTitle(), textStyle).getY();
        int startIndex = Math.max(menu.getActiveButtonPosition() - Constants.AFTER_ACTIVE_BUTTONS_COUNT, 0);
        int endIngex = Math.min(menu.getActiveButtonPosition() - Constants.AFTER_ACTIVE_BUTTONS_COUNT,
                menu.getButtons().size() - 1);
        for (int i = startIndex; i < endIngex + 1; i++) {
            horizontalOffset += Constants.MENU_BUTTON_HORIZONTAL_INDENT_PX;
            MenuButton button = menu.getButtons().get(i);
            textStyle = menu.isActiveButton(button) ? style.getMenuActiveTextStyle() : style.getMenuButtonTextStyle();
            horizontalOffset += drawText(context,
                    menuTextCalculatePosition(
                            button.getText(), textStyle,
                            Point.with(Constants.MENU_BUTTON_HORIZONTAL_INDENT_PX, horizontalOffset),
                            canvasSize
                    ),
                    button.getText(), textStyle).getY();
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
