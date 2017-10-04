package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class PolarGridGunnerAgent extends GunnerAgent {
    private final PolarGridDescriptor polarGridDescriptor;

    public PolarGridGunnerAgent(PolarGridDescriptor polarGridDescriptor) {
        this.polarGridDescriptor = polarGridDescriptor;
    }

    @Override
    public final GunnerPlayer buildPlayer(long id) {
        return buildPlayer(id, polarGridDescriptor);
    }

    protected abstract PolarGridAgentGunnerPlayer buildPlayer(long id, PolarGridDescriptor polarGridDescriptor);
}
