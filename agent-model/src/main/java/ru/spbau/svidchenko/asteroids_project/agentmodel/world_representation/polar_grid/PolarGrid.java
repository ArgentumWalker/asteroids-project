package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.List;

public class PolarGrid {
    private PolarGridDescriptor polarGridDescriptor;
    private List<List<Boolean>> values = new ArrayList<>();

    public PolarGrid(PolarGridDescriptor polarGridDescriptor) {
        this.polarGridDescriptor = polarGridDescriptor;
        for (int i = 0; i < polarGridDescriptor.getAngleSectors().size() + 1; i++) {
            List<Boolean> valuesAtAngle = new ArrayList<>();
            for (int j = 0; j < polarGridDescriptor.getDistanceSectors().size() + 1; j++) {
                valuesAtAngle.add(false);
            }
            values.add(valuesAtAngle);
        }
    }

    public void refresh(RelativeWorldModel worldModel) {
        for (int i = 0; i < polarGridDescriptor.getAngleSectors().size() + 1; i++) {
            for (int j = 0; j < polarGridDescriptor.getDistanceSectors().size(); j++) {
                values.get(i).set(j, false);
            }
        }
        for (EntityRelative relative : worldModel.getRelatives()) {
            if (relative.getEntity() instanceof Stone) {
                int distanceSector = calculateDistanceSector(relative);
                if (distanceSector < polarGridDescriptor.getDistanceSectors().size()) {
                    values.get(calculateAngleSector(relative)).set(distanceSector, true);
                }
            }
            //TODO: add bullets
        }
    }

    private int calculateAngleSector(EntityRelative relative) {
        int result = 0;
        long angle = (long) (relative.getPosition().getAngle() / (2 * Math.PI * Constants.WEAPON_TURNS_TO_TURN_AROUND));
        for (long border : polarGridDescriptor.getAngleSectors()) {
            if (border > angle) {
                return result;
            }
            result++;
        }
        return result;
    }


    private int calculateDistanceSector(EntityRelative relative) {
        int result = 0;
        double distance = relative.getPosition().worldDistanceTo(Point.with(0, 0));
        for (double border : polarGridDescriptor.getDistanceSectors()) {
            if (border > distance) {
                return result;
            }
            result++;
        }
        return result;
    }

    public List<List<Boolean>> getValues() {
        return values;
    }

    public PolarGridDescriptor getPolarGridDescriptor() {
        return polarGridDescriptor;
    }
}
