package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SortedEntitiesDataDescriptor implements Serializable {
    private List<Long> angleSectors = new ArrayList<>();
    private List<Double> distanceSectors = new ArrayList<>();
    private long limit = 1;
    private long maxAngle = 80;
    private boolean vehicleRelated = false;
    private boolean reloadRelated = false;
    private double closerReward = 0;
    private boolean distanceVehicleRelated = false;
    private boolean disableSymmetry = false;

    public void setVehicleRelated(boolean vehicleRelated) {
        this.vehicleRelated = vehicleRelated;
    }

    public void setReloadRelated(boolean reloadRelated) {
        this.reloadRelated = reloadRelated;
    }

    public void setMaxAngle(long maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void splitAngle(long at) {
        if (maxAngle > at && at > 0) {
            int position = 0;
            while (position < angleSectors.size() && angleSectors.get(position) < at) {
                position++;
            }
            angleSectors.add(position, at);
        }
    }

    public void splitDistance(double at) {
        if (at > 0) {
            int position = 0;
            while (position < distanceSectors.size() && distanceSectors.get(position) < at) {
                position++;
            }
            distanceSectors.add(position, at);
        }
    }
    public long getMaxAngle() {
        return maxAngle;
    }

    public List<Long> getAngleSectors() {
        return angleSectors;
    }

    public List<Double> getDistanceSectors() {
        return distanceSectors;
    }

    public long getLimit() {
        return limit;
    }

    public boolean isReloadRelated() {
        return reloadRelated;
    }

    public boolean isVehicleRelated() {
        return vehicleRelated;
    }

    public double getCloserReward() {
        return closerReward;
    }

    public void setCloserReward(double closerReward) {
        this.closerReward = closerReward;
    }

    public boolean isDistanceVehicleRelated() {
        return distanceVehicleRelated;
    }

    public void setDistanceVehicleRelated(boolean distanceVehicleRelated) {
        this.distanceVehicleRelated = distanceVehicleRelated;
    }

    public boolean isDisableSymmetry() {
        return disableSymmetry;
    }

    public void setDisableSymmetry(boolean disableSymmetry) {
        this.disableSymmetry = disableSymmetry;
    }
}
