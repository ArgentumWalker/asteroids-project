package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SortedEntitiesData {
    private final SortedEntitiesDataDescriptor descriptor;
    private long currentState;

    public SortedEntitiesData(SortedEntitiesDataDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void refresh(RelativeWorldModel worldModel) {
        currentState = 0;
        Point zero = Point.with(0,0);
        worldModel.getRelatives().stream()
                .filter(rel -> rel instanceof Stone.Relative)
                .sorted(Comparator.comparing(rel -> rel.getPosition().worldDistanceTo(zero)))
                .limit(descriptor.getLimit())
                .collect(Collectors.toList())
                .forEach(rel -> currentState = currentState * (descriptor.getAngleSectors().size() + 1) + calculateAngleSector(rel));
    }

    public long getCurrentState() {
        return currentState;
    }

    private int calculateAngleSector(EntityRelative relative) {
        int result = 0;
        double realAngle = relative.getPosition().getAngle();
        long angle = (long) (descriptor.getMaxAngle() * (realAngle + (realAngle < 0 ? 2 * Math.PI : 0)) / (2 * Math.PI));
        for (long border : descriptor.getAngleSectors()) {
            if (border > angle) {
                return result;
            }
            result++;
        }
        return result;
    }
}
