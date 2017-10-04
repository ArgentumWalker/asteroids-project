package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.plane_grid;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentPilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class PlaneGridPilotAgent extends PilotAgent {
    private final PlaneGridDescriptor planeGridDescriptor;

    public PlaneGridPilotAgent(PlaneGridDescriptor planeGridDescriptor) {
        this.planeGridDescriptor = planeGridDescriptor;
    }

    @Override
    public final PilotPlayer buildPlayer(long id) {
        return buildPlayer(id, planeGridDescriptor);
    }

    protected abstract PolarGridAgentPilotPlayer buildPlayer(long id, PlaneGridDescriptor planeGridDescriptor);
}
