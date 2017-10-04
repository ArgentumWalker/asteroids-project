package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class PolarGridAgentPilotPlayer extends PilotPlayer {
    private PolarGrid polarGrid;

    public PolarGridAgentPilotPlayer(long id, PolarGridDescriptor polarGridDescriptor) {
        super(id);
        polarGridDescriptor.setMaxAngle(Constants.VEHICLE_TURNS_TO_TURN_AROUND);
        this.polarGrid = new PolarGrid(polarGridDescriptor);
    }

    protected final Action chooseAction() {
        polarGrid.refresh(worldModel);
        return chooseAction(polarGrid);
    }

    protected abstract Action chooseAction(PolarGrid polarGrid);
}
