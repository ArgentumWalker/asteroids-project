package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;

public class SplitScreenGame extends KeyboardControls {

    private GraphicsContext pilotContext;
    private GraphicsContext gunnerContext;

    protected SplitScreenGame(Stage stage) {
        super(stage);
    }

    @Override
    public void start(WorldDescriptor descriptor, ClientMenu menu, GraphicStyleContainer style, PilotPlayer pilot, GunnerPlayer gunner) {
        stage.setWidth(Constants.WINDOW_HALF_WIDTH_PX * 4);
        stage.setHeight(Constants.WINDOW_HALF_HEIGHT_PX * 2);
        super.start(descriptor, menu, style, pilot, gunner);
    }

    @Override
    protected Scene initScene() {
        Group root = new Group();

        Canvas pilotCanvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WORLD_HALF_HEIGHT * 2);
        Canvas gunnerCanvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WORLD_HALF_HEIGHT * 2);

        pilotContext = pilotCanvas.getGraphicsContext2D();
        gunnerContext = gunnerCanvas.getGraphicsContext2D();

        HBox box = new HBox(0.0, pilotCanvas, gunnerCanvas);

        root.getChildren().add(box);
        return new Scene(root);
    }

    @Override
    protected AnimationTimer buildAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicUtils.drawGameBackground(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                GraphicUtils.drawWorld(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                GraphicUtils.drawUi(pilotContext, currentShipCrew.getMembers().first().getWorldModel(), style, currentShipCrew);
                GraphicUtils.drawGameBackground(gunnerContext, currentShipCrew.getMembers().second().getWorldModel(), style);
                GraphicUtils.drawWorld(gunnerContext, currentShipCrew.getMembers().second().getWorldModel(), style);
                GraphicUtils.drawUi(gunnerContext, currentShipCrew.getMembers().second().getWorldModel(), style, currentShipCrew);
            }
        };
    }
}
