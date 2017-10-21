package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.tensor;

import org.tensorflow.Tensor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;

public abstract class TensorAgentGunnerPlayer extends GunnerPlayer{

    public TensorAgentGunnerPlayer(long id) {
        super(id);
    }

    @Override
    protected final Action chooseAction() {
        return chooseAction(TensorBuilder.buildTensor(worldModel, weapon.getShip()));
    }

    protected abstract Action chooseAction(TensorBuilder.WorldTensors tensors);

}
