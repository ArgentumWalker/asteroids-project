package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PolarGridDescriptor implements Serializable {
    private List<Long> angleSectors = new ArrayList<>();
    private List<Double> distanceSectors = new ArrayList<>();
    private long maxAngle = 0;

    public void setMaxAngle(long maxAngle) {
        this.maxAngle = maxAngle;
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
}
