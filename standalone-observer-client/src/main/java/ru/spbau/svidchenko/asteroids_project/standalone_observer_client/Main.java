package ru.spbau.svidchenko.asteroids_project.standalone_observer_client;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(Constants.WINDOW_HALF_WIDTH_PX * 2);
        primaryStage.setHeight(Constants.WINDOW_HALF_HEIGHT_PX * 2);
        primaryStage.setResizable(false);

        GameClient gameClient = new GameClient(primaryStage);
        ObserverMenu observerMenu = new ObserverMenu(primaryStage, gameClient);
        observerMenu.start();

        primaryStage.show();
    }

}
