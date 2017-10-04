package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.plane_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class PlaneGridAgentPilotPlayer extends PilotPlayer {
    private PlaneGrid planeGrid;

    public PlaneGridAgentPilotPlayer(long id, PlaneGridDescriptor planeGridDescriptor) {
        super(id);
        this.planeGrid = new PlaneGrid(planeGridDescriptor, worldModel);
    }

    protected final Action chooseAction() {
        planeGrid.refresh();
        return chooseAction(planeGrid);
    }

    protected abstract Action chooseAction(PlaneGrid planeGrid);
}
