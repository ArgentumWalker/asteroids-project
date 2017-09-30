package ru.spbau.svidchenko.asteroids_project.agentmodel.base.plane_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.List;

public class PlaneGrid {
    private PlaneGridDescriptor planeGridDescriptor;
    private RelativeWorldModel worldModel;
    private List<List<Boolean>> values = new ArrayList<>();

    public PlaneGrid(PlaneGridDescriptor planeGridDescriptor, RelativeWorldModel worldModel) {
        this.planeGridDescriptor = planeGridDescriptor;
        this.worldModel = worldModel;
        for (int i = 0; i < planeGridDescriptor.getXSectors().size() + 1; i++) {
            List<Boolean> valuesAtAngle = new ArrayList<>();
            for (int j = 0; j < planeGridDescriptor.getYSectors().size() + 1; j++) {
                valuesAtAngle.add(false);
            }
            values.add(valuesAtAngle);
        }
        refresh();
    }

    public void refresh() {
        for (int i = 0; i < planeGridDescriptor.getXSectors().size() + 1; i++) {
            for (int j = 0; j < planeGridDescriptor.getYSectors().size() + 1; j++) {
                values.get(i).set(i, false);
            }
        }
        for (EntityRelative relative : worldModel.getRelatives()) {
            if (relative.getEntity() instanceof Stone) {
                values.get(calculateXSector(relative)).set(calculateYSector(relative), true);
            }
            //TODO: add bullets
        }
    }

    private int calculateXSector(EntityRelative relative) {
        int result = 0;
        double x = relative.getPosition().getX();
        for (double border : planeGridDescriptor.getXSectors()) {
            if (border > x) {
                return result;
            }
            result++;
        }
        return result;
    }


    private int calculateYSector(EntityRelative relative) {
        int result = 0;
        double y = relative.getPosition().getY();
        for (double border : planeGridDescriptor.getYSectors()) {
            if (border > y) {
                return result;
            }
            result++;
        }
        return result;
    }

    public List<List<Boolean>> getValues() {
        return values;
    }

    public PlaneGridDescriptor getPlaneGridDescriptor() {
        return planeGridDescriptor;
    }
}
