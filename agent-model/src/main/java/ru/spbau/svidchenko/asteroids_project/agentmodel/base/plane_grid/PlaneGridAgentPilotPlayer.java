package ru.spbau.svidchenko.asteroids_project.agentmodel.base.plane_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public abstract class PlaneGridAgentPilotPlayer extends GunnerPlayer {
    private PlaneGrid planeGrid;

    public PlaneGridAgentPilotPlayer(long id, PlaneGridDescriptor planeGridDescriptor) {
        super(id);
        planeGridDescriptor.setMaxAngle(Constants.VEHICLE_MOVES_TO_TURN);
        this.planeGrid = new PlaneGrid(planeGridDescriptor, worldModel);
    }

    protected final Action chooseAction() {
        planeGrid.refresh();
        return chooseAction(planeGrid);
    }

    protected abstract Action chooseAction(PlaneGrid planeGrid);
}