package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.tensor;

import org.tensorflow.Tensor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

public abstract class TensorAgentPilotPlayer extends PilotPlayer {

    public TensorAgentPilotPlayer(long id) {
        super(id);
    }

    @Override
    protected final Action chooseAction() {
        return chooseAction(TensorBuilder.buildTensor(worldModel, vehicle.getShip()));
    }

    protected abstract Action chooseAction(TensorBuilder.WorldTensors tensors);
}
