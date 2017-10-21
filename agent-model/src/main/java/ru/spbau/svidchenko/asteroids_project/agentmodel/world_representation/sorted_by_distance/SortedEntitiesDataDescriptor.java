package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortedEntitiesDataDescriptor implements Serializable {
    private List<Long> angleSectors = new ArrayList<>();
    private long limit = 0;
    private long maxAngle = 0;

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

    public long getMaxAngle() {
        return maxAngle;
    }

    public List<Long> getAngleSectors() {
        return angleSectors;
    }

    public long getLimit() {
        return limit;
    }
}
