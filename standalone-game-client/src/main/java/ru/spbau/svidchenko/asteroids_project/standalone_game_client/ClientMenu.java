package ru.spbau.svidchenko.asteroids_project.standalone_game_client;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentSaveLoader;
import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentsBuilder;
import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicUtils;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.Menu;
import ru.spbau.svidchenko.asteroids_project.graphics_common.menu.MenuButton;
import ru.spbau.svidchenko.asteroids_project.graphics_common.styles.NeonGraphicStyle;

import java.util.ArrayList;
import java.util.List;

public class ClientMenu {
    private final Stage stage;
    private final Scene scene;
    private final GraphicsContext context;
    private final OnePlayerGame onePlayerGame;
    private final SplitScreenGame splitScreenGame;
    private final Menu mainMenu = new Menu("Neon Sparks");
    private final Menu selectYourRole = new Menu("Choose your role");
    private final Menu selectGunnerMenu = new Menu("Choose Gunner");
    private final Menu selectPilotMenu = new Menu("Choose Pilot");

    private Menu currentMenu = mainMenu;
    private GraphicStyleContainer style = new NeonGraphicStyle();
    private List<GunnerAgent> gunnerAgents = new ArrayList<>();
    private List<PilotAgent> pilotAgents = new ArrayList<>();

    public ClientMenu(Stage stage, OnePlayerGame onePlayerGame, SplitScreenGame splitScreenGame) {
        this.onePlayerGame = onePlayerGame;
        this.splitScreenGame = splitScreenGame;
        this.stage = stage;
        Group root = new Group();
        scene = new Scene(root);
        Canvas canvas = new Canvas(Constants.WINDOW_HALF_WIDTH_PX * 2, Constants.WINDOW_HALF_HEIGHT_PX * 2);
        this.context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        initAgents();
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
        splitScreenGame.start(worldDescriptor, this, style,
                splitScreenGame.getPilot(1), splitScreenGame.getGunner(2));
    }

    private void startOnePlayerGame() {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        onePlayerGame.start(worldDescriptor, this, style,
                onePlayerGame.getPilot(1), onePlayerGame.getGunner(2));
    }

    private void startGameWith(GunnerAgent agent) {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        onePlayerGame.start(worldDescriptor, this, style, true,
                onePlayerGame.getPilot(1), agent.buildPlayer(2));
    }

    private void startGameWith(PilotAgent agent) {
        WorldDescriptor worldDescriptor = new WorldDescriptor();
        onePlayerGame.start(worldDescriptor, this, style, false,
                agent.buildPlayer(1), onePlayerGame.getGunner(2));
    }

    private void exit() {
        //TODO: Exit
    }

    private void showSelectGunner() {
        currentMenu = selectGunnerMenu;
        refresh();
    }

    private void showSelectPilot() {
        currentMenu = selectPilotMenu;
        refresh();
    }

    private void showChooseRole() {
        currentMenu = selectYourRole;
        refresh();
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
                        showChooseRole();
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
        for (GunnerAgent agent : gunnerAgents) {
            selectGunnerMenu.addButton(
                    new MenuButton(agent.getName()) {
                        @Override
                        public void onClick() {
                            startGameWith(agent);
                        }
                    }
            );
        }
        for (PilotAgent agent : pilotAgents) {
            selectPilotMenu.addButton(
                    new MenuButton(agent.getName()) {
                        @Override
                        public void onClick() {
                            startGameWith(agent);
                        }
                    }
            );
        }
        selectYourRole.addButton(
                new MenuButton("Pilot") {
                    @Override
                    public void onClick() {
                        showSelectGunner();
                    }
                }
        ).addButton(
                new MenuButton("Gunner") {
                    @Override
                    public void onClick() {
                        showSelectPilot();
                    }
                }
        ).addButton(
                new MenuButton("Both") {
                    @Override
                    public void onClick() {
                        startOnePlayerGame();
                    }
                }
        );
    }

    private void initAgents() {
        //Gunners
        try {
            gunnerAgents.addAll(AgentSaveLoader.loadGunners());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gunnerAgents.addAll(AgentsBuilder.getDefaultGunnerAgents());
        gunnerAgents.forEach(GunnerAgent::disableLearning);
        //Pilots
        try {
            pilotAgents.addAll(AgentSaveLoader.loadPilots());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pilotAgents.addAll(AgentsBuilder.getDefaultPilotAgents());
        pilotAgents.addAll(AgentsBuilder.getImprovedPilotAgents());
        pilotAgents.forEach(PilotAgent::disableLearning);
    }
}
