package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.tensor;

import org.tensorflow.Tensor;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

class TensorBuilder {
    public static WorldTensors buildTensor(RelativeWorldModel worldModel, Ship ship) {
        Point zero = Point.with(0, 0);
        List<Double> stones = new ArrayList<>();
        List<Double> ships = new ArrayList<>();
        List<Double> bullets = new ArrayList<>();
        List<Double> myShip = new ArrayList<>();
        worldModel.readLock().lock();
        for (EntityRelative relative : worldModel.getRelatives()) {
            //Base
            List<Double> tmpResult = new ArrayList<>();
            tmpResult.add(relative.getPosition().getX());
            tmpResult.add(relative.getPosition().getY());
            double angle = relative.getPosition().getAngle();
            tmpResult.add(angle);
            tmpResult.add(Math.sin(angle));
            tmpResult.add(Math.cos(angle));
            tmpResult.add(relative.getPosition().worldDistanceTo(zero));
            tmpResult.add((double)relative.getEntity().getRemainingHealth());
            tmpResult.add(relative.getEntity().getVelocity().worldDistanceTo(zero));
            angle = relative.getEntity().getVelocity().getAngle() - worldModel.getCurrentAngle();
            tmpResult.add(angle);
            tmpResult.add(Math.sin(angle));
            tmpResult.add(Math.cos(angle));
            tmpResult.add(relative.getEntity().harmfulImpactsTo(ship) ? -1.0 : 1.0);
            if (relative instanceof Stone.Relative) {
                stones.addAll(tmpResult);
                continue;
            }
            if (relative instanceof Ship.Relative) {
                angle = ((Ship.Relative) relative).getVehicleOrientation();
                tmpResult.add(Math.sin(angle));
                tmpResult.add(Math.cos(angle));
                angle = ((Ship.Relative) relative).getWeaponOrientation();
                tmpResult.add(Math.sin(angle));
                tmpResult.add(Math.cos(angle));
                if (((Ship.Relative) relative).getEntity().equals(ship)) {
                    myShip.addAll(tmpResult);
                } else {
                    ships.addAll(tmpResult);
                }
                continue;
            }
            if (relative instanceof Bullet.Relative) {
                tmpResult.add(Double.valueOf(relative.getEntity().getRemainingHealth()) / Constants.BULLET_DAMAGE_PER_MOVE);
                bullets.addAll(tmpResult);
                continue;
            }
        }
        worldModel.readLock().unlock();
        DoubleBuffer bulletsBuffer = DoubleBuffer.allocate(bullets.size());
        bulletsBuffer.put(bullets.stream().mapToDouble(d -> d).toArray());
        long[] bulletsShape = {bullets.size() / 13, 13};
        DoubleBuffer stonesBuffer = DoubleBuffer.allocate(stones.size());
        stonesBuffer.put(stones.stream().mapToDouble(d -> d).toArray());
        long[] stonesShape = {bullets.size() / 12, 12};
        DoubleBuffer shipsBuffer = DoubleBuffer.allocate(ships.size());
        shipsBuffer.put(ships.stream().mapToDouble(d -> d).toArray());
        long[] shipsShape = {bullets.size() / 16, 16};
        DoubleBuffer myShipBuffer = DoubleBuffer.allocate(16);
        shipsBuffer.put(myShip.stream().mapToDouble(d -> d).toArray());
        long[] myShipShape = {16};

        WorldTensors tensors = new WorldTensors();
        tensors.bullets = Tensor.create(bulletsShape, bulletsBuffer);
        tensors.stones = Tensor.create(stonesShape, stonesBuffer);
        tensors.ships = Tensor.create(shipsShape, shipsBuffer);
        tensors.myShip = Tensor.create(myShipShape, myShipBuffer);
        return tensors;
    }

    public static class WorldTensors {
        public Tensor stones;
        public Tensor bullets;
        public Tensor ships;
        public Tensor myShip;
    }
}
