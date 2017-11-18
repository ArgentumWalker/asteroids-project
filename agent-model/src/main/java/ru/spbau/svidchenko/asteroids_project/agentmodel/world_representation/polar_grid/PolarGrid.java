package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PolarGrid {
    private PolarGridDescriptor polarGridDescriptor;
    private List<List<Integer>> values = new ArrayList<>();

    public PolarGrid(PolarGridDescriptor polarGridDescriptor) {
        this.polarGridDescriptor = polarGridDescriptor;
        for (int i = 0; i < polarGridDescriptor.getAngleSectors().size() + 1; i++) {
            List<Integer> valuesAtAngle = new ArrayList<>();
            for (int j = 0; j < polarGridDescriptor.getDistanceSectors().size(); j++) {
                valuesAtAngle.add(0);
            }
            values.add(valuesAtAngle);
        }
    }

    public void refresh(RelativeWorldModel worldModel) {
        for (int i = 0; i < polarGridDescriptor.getAngleSectors().size() + 1; i++) {
            for (int j = 0; j < polarGridDescriptor.getDistanceSectors().size(); j++) {
                values.get(i).set(j, 0);
            }
        }
        Set<EntityRelative> relatives = worldModel.getRelatives().stream()
                .filter(rel -> rel instanceof Stone.Relative)
                .collect(Collectors.toSet());
        if (polarGridDescriptor.getLimit() != 0) {
            Point zero = Point.with(0,0);
            relatives = relatives.stream()
                    .sorted(Comparator.comparing(rel -> rel.getPosition().worldDistanceTo(zero)))
                    .limit(polarGridDescriptor.getLimit())
                    .collect(Collectors.toSet());
        }
        for (EntityRelative relative : relatives) {
            int distanceSector = calculateDistanceSector(relative);
            if (distanceSector < polarGridDescriptor.getDistanceSectors().size()) {
                int val = values.get(calculateAngleSector(relative)).get(distanceSector);
                values.get(calculateAngleSector(relative)).set(distanceSector, val + 1);
            }
            //TODO: add bullets
        }
    }

    private int calculateAngleSector(EntityRelative relative) {
        int result = 0;
        double realAngle = relative.getPosition().getAngle();
        long angle = (long) (polarGridDescriptor.getMaxAngle() * (realAngle + (realAngle < 0 ? 2 * Math.PI : 0)) / (2 * Math.PI));
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

    public List<List<Integer>> getValues() {
        return values;
    }

    public PolarGridDescriptor getPolarGridDescriptor() {
        return polarGridDescriptor;
    }
}
