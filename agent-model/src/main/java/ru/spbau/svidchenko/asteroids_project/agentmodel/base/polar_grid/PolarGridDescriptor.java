package ru.spbau.svidchenko.asteroids_project.agentmodel.base.polar_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;

import java.util.ArrayList;
import java.util.List;

public class PolarGridDescriptor {
    private List<Long> angleSectors = new ArrayList<>();
    private List<Double> distanceSectors = new ArrayList<>();
    private long maxAngle = 0;

    public void setMaxAngle(long maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void splitAngle(long at) {
        if (maxAngle > at && at > 0) {
            int position = 0;
            while (angleSectors.get(position) < at) {
                position++;
            }
            angleSectors.add(position, at);
        }
    }

    public void splitDistance(double at) {
        if (at > 0) {
            int position = 0;
            while (distanceSectors.get(position) < at) {
                position++;
            }
            distanceSectors.add(position, at);
        }
    }

    public List<Long> getAngleSectors() {
        return angleSectors;
    }

    public List<Double> getDistanceSectors() {
        return distanceSectors;
    }
}
