package ru.spbau.svidchenko.asteroids_project.standalone_game_client.standalone_app;

import javafx.scene.canvas.GraphicsContext;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;

public interface StandaloneClientInterface {
    //Game events
    void onGameStart();
    void onGameEnd();
    boolean isGameInterrupted();
}
