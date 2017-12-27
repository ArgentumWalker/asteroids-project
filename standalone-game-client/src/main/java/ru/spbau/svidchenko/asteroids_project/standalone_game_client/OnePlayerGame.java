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

public class OnePlayerGame extends KeyboardControls {

    private GraphicsContext playerContext;
    private boolean pilotView = true;

    protected OnePlayerGame(Stage stage) {
        super(stage);
    }

    public void start(WorldDescriptor descriptor, ClientMenu menu, GraphicStyleContainer style, boolean pilotView, PilotPlayer pilot, GunnerPlayer gunner) {
        this.pilotView = pilotView;
        start(descriptor, menu, style, pilot, gunner);
    }

    @Override
    public void start(WorldDescriptor descriptor, ClientMenu menu, GraphicStyleContainer style, PilotPlayer pilot, GunnerPlayer gunner) {
        stage.setWidth(Constants.WINDOW_HALF_WIDTH_PX * 2);
        stage.setHeight(Constants.WINDOW_HALF_HEIGHT_PX * 2);
        super.start(descriptor, menu, style, pilot, gunner);
    }

    @Override
    protected Scene initScene() {
        Group root = new Group();
        Canvas canvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WORLD_HALF_HEIGHT * 2);
        playerContext = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        return new Scene(root);
    }

    @Override
    protected AnimationTimer buildAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pilotView) {
                    GraphicUtils.drawGameBackground(playerContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                    GraphicUtils.drawWorld(playerContext, currentShipCrew.getMembers().first().getWorldModel(), style);
                    GraphicUtils.drawUi(playerContext, currentShipCrew.getMembers().first().getWorldModel(), style, currentShipCrew);
                } else {
                    GraphicUtils.drawGameBackground(playerContext, currentShipCrew.getMembers().second().getWorldModel(), style);
                    GraphicUtils.drawWorld(playerContext, currentShipCrew.getMembers().second().getWorldModel(), style);
                    GraphicUtils.drawUi(playerContext, currentShipCrew.getMembers().second().getWorldModel(), style, currentShipCrew);
                }
            }
        };
    }
}
