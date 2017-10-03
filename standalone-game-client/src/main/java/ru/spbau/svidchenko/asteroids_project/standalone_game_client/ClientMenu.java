package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.Menu;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.MenuButton;
import ru.spbau.svidchenko.asteroids_project.graphics_common.styles.NeonGraphicStyle;

public class ClientMenu {
    private final Stage stage;
    private final Scene scene;
    private final GraphicsContext context;
    private final OnePlayerGame onePlayerGame;
    private final SplitScreenGame splitScreenGame;
    private final Menu mainMenu = new Menu("Neon Sparks");

    private Menu currentMenu = mainMenu;
    private GraphicStyleContainer style = new NeonGraphicStyle();

    public ClientMenu(Stage stage, OnePlayerGame onePlayerGame, SplitScreenGame splitScreenGame) {
        this.onePlayerGame = onePlayerGame;
        this.splitScreenGame = splitScreenGame;
        this.stage = stage;
        Group root = new Group();
        scene = new Scene(root);
        Canvas canvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WINDOW_HALF_HEIGHT_PX * 2);
        this.context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        initMenus();
        initEventHandler();
    }

    public void start() {
        stage.setResizable(true);
        stage.setWidth(Constants.WINDOW_HALF_WIDTH_PX * 2);
        stage.setHeight(Constants.WINDOW_HALF_HEIGHT_PX * 2);
        stage.setScene(scene);
        stage.setResizable(false);
        showMainMenu();
    }

    private void showMainMenu() {
        currentMenu = mainMenu;
        refresh();
    }

    private void startSplitScreenGame() {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        splitScreenGame.start(worldDescriptor, this, 1, 2, style);
    }

    private void startOnePlayerGame() {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        onePlayerGame.start(worldDescriptor, this, 1, 2, style);
    }

    private void exit() {
        //TODO: Exit
    }

    private void showSelectGunner() {
        //TODO: implement player selector
    }

    private void showSelectPilot() {
        //TODO: implement player selector
    }

    private void refresh() {
        GraphicUtils.drawMenu(context, currentMenu, style);
    }

    private void initEventHandler() {
        scene.setOnKeyPressed(
                event -> {
                    switch (event.getCode()) {
                        case UP: {
                            currentMenu.activatePreviousButton();
                            refresh();
                            break;
                        }
                        case DOWN: {
                            currentMenu.activateNextButton();
                            refresh();
                            break;
                        }
                        case ENTER: {
                            currentMenu.getActiveButton().onClick();
                            break;
                        }
                    }
                }
        );
    }

    private void initMenus() {
        mainMenu.addButton(
                new MenuButton("One Player") {
                    @Override
                    public void onClick() {
                        startOnePlayerGame();
                    }
                }
        ).addButton(
                new MenuButton("Split Screen") {
                    @Override
                    public void onClick() {
                        startSplitScreenGame();
                    }
                }
        ).addButton(
                new MenuButton("Quit") {
                    @Override
                    public void onClick() {
                        exit();
                    }
                }
        );

    }
}
