package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class PolarGridPilotAgent extends PilotAgent {
    private final PolarGridDescriptor polarGridDescriptor;

    public PolarGridPilotAgent(PolarGridDescriptor polarGridDescriptor) {
        this.polarGridDescriptor = polarGridDescriptor;
    }

    @Override
    public final PilotPlayer buildPlayer(long id) {
        return buildPlayer(id, polarGridDescriptor);
    }

    protected abstract PolarGridAgentPilotPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor);
}
