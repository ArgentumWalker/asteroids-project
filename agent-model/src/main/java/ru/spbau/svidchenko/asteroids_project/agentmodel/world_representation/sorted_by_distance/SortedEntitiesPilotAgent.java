package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class SortedEntitiesPilotAgent extends PilotAgent {
    private final SortedEntitiesDataDescriptor descriptor;

    protected SortedEntitiesPilotAgent(SortedEntitiesDataDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public final PilotPlayer buildPlayer(long id) {
        return buildPlayer(id, descriptor);
    }

    protected abstract PilotPlayer buildPlayer(long id, SortedEntitiesDataDescriptor descriptor);

    public static abstract class Pilot extends PilotPlayer {
        private final SortedEntitiesData data;

        public Pilot(long id, SortedEntitiesDataDescriptor descriptor) {
            super(id);
            data = new SortedEntitiesData(descriptor);
        }

        protected final Action chooseAction() {
            data.refresh(worldModel);
            return chooseAction(data);
        }

        protected abstract Action chooseAction(SortedEntitiesData data);
    }
}
