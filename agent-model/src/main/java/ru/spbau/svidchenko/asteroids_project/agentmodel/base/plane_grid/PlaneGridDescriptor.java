package ru.spbau.svidchenko.asteroids_project.agentmodel.base.plane_grid;

import java.util.ArrayList;
import java.util.List;

public class PlaneGridDescriptor {
    private List<Double> xSectors = new ArrayList<>();
    private List<Double> ySectors = new ArrayList<>();

    public void splitX(double at) {
        int position = 0;
        while (xSectors.get(position) < at) {
            position++;
        }
        xSectors.add(position, at);
    }

    public void splitY(double at) {
        int position = 0;
        while (ySectors.get(position) < at) {
            position++;
        }
        ySectors.add(position, at);
    }

    public List<Double> getXSectors() {
        return xSectors;
    }

    public List<Double> getYSectors() {
        return ySectors;
    }
}
