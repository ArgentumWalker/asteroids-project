package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public abstract class SortedEntitiesGunnerAgent extends GunnerAgent {
    private final SortedEntitiesDataDescriptor descriptor;

    protected SortedEntitiesGunnerAgent(SortedEntitiesDataDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public final GunnerPlayer buildPlayer(long id) {
        return buildPlayer(id, descriptor);
    }

    protected abstract GunnerPlayer buildPlayer(long id, SortedEntitiesDataDescriptor descriptor);

    public static abstract class Gunner extends GunnerPlayer {
        private final SortedEntitiesData data;

        public Gunner(long id, SortedEntitiesDataDescriptor descriptor) {
            super(id);
            data = new SortedEntitiesData(descriptor);
        }

        public final Action chooseAction() {
            data.refresh(worldModel);
            return chooseAction(data);
        }

        protected abstract Action chooseAction(SortedEntitiesData data);
    }
}
