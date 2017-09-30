package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public abstract class PolarGridAgentGunnerPlayer extends GunnerPlayer {
    private PolarGrid polarGrid;

    public PolarGridAgentGunnerPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        super(id);
        polarGridDescriptor.setMaxAngle(Constants.VEHICLE_MOVES_TO_TURN);
        this.polarGrid = new PolarGrid(polarGridDescriptor, worldModel);
    }

    protected final Action chooseAction() {
        polarGrid.refresh();
        return chooseAction(polarGrid);
    }

    protected abstract Action chooseAction(PolarGrid polarGrid);
}
