package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.plane_grid;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridAgentGunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public abstract class PlaneGridGunnerAgent extends GunnerAgent {
    private final PlaneGridDescriptor planeGridDescriptor;

    public PlaneGridGunnerAgent(PlaneGridDescriptor planeGridDescriptor) {
        this.planeGridDescriptor = planeGridDescriptor;
    }

    @Override
    public final GunnerPlayer buildPlayer(long id) {
        return buildPlayer(id, planeGridDescriptor);
    }

    protected abstract PolarGridAgentGunnerPlayer buildPlayer(long id, PlaneGridDescriptor planeGridDescriptor);
}
