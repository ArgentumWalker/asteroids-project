package ru.spbau.svidchenko.asteroids_project.standalone_observer_client;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.DoNothingGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.ActionFunctions;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.AvoidPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.improved.PowerFunctions;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.Menu;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.MenuButton;
import ru.spbau.svidchenko.asteroids_project.graphics_common.styles.NeonGraphicStyle;

import java.io.IOException;

public class ObserverMenu {
    private final Stage stage;
    private final Scene scene;
    private final GraphicsContext context;
    private final GameClient gameClient;
    private final Menu mainMenu = new Menu("Neon Sparks");
    private final Menu selectCrew = new Menu("Edit crew players list");
    private final Menu selectGunner = new Menu("Select gunner");
    private final Menu selectPilot = new Menu("Select pilot");

    private Menu currentMenu = mainMenu;
    private GraphicStyleContainer style = new NeonGraphicStyle();

    //TODO: implement player selector
    //TMP
    private PilotAgent pilotAgent = new AvoidPilotAgent(
            PowerFunctions.getSquaredPowerFunction(2.5e4),
            Point.with(0, 0),
            ActionFunctions.getSinActionFunction(1, 0.5), "test");//AgentSaveLoader.loadPilots().get(1);
    private PilotPlayer pilotPlayer = pilotAgent.buildPlayer(1);
    //private GunnerPlayer gunnerPlayer = AgentSaveLoader.loadGunners().get(3).buildPlayer(2);
    //private PilotPlayer pilotPlayer = new AgentsBuilder().getDefaultPilotAgents().get(0).buildPlayer(1);
    private GunnerPlayer gunnerPlayer = new DoNothingGunnerAgent().buildPlayer(2);
    //TMP

    public ObserverMenu(Stage stage, GameClient gameClient) throws IOException, ClassNotFoundException {
        pilotAgent.disableLearning();

        this.gameClient = gameClient;
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
        stage.setScene(scene);
        showMainMenu();
    }

    private void showMainMenu() {
        currentMenu = mainMenu;
        refresh();
    }

    private void showSelectCrew() {
        currentMenu = selectCrew;
        refresh();
    }

    private void startGame() {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        worldDescriptor.players.add(new ShipCrew(pilotPlayer, gunnerPlayer));
        gameClient.start(worldDescriptor, style, this);
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
                new MenuButton("New Game") {
                    @Override
                    public void onClick() {
                        showSelectCrew();
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
        selectCrew.addButton(
                new MenuButton("Select Gunner") {
                    @Override
                    public void onClick() {
                        showSelectGunner();
                    }
                }
        ).addButton(
                new MenuButton("Select Pilot") {
                    @Override
                    public void onClick() {
                        showSelectPilot();
                    }
                }
        ).addButton(
                new MenuButton("Start") {
                    @Override
                    public void onClick() {
                        startGame();
                    }
                }
        ).addButton(
                new MenuButton("Back") {
                    @Override
                    public void onClick() {
                        showMainMenu();
                    }
                }
        );
        //TODO PLAYER LOADER
        selectGunner.addButton(
                new MenuButton("Back") {
                    @Override
                    public void onClick() {
                        showSelectCrew();
                    }
                }
        );
        selectPilot.addButton(
                new MenuButton("Back") {
                    @Override
                    public void onClick() {
                        showSelectCrew();
                    }
                }
        );

    }


}
