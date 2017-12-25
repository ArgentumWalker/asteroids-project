package ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.sorted_by_distance;

import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.commons.Point;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.RelativeWorldModel;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Ship;
import ru.spbau.svidchenko.asteroids_project.game_logic.world.Stone;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortedEntitiesData {
    private final SortedEntitiesDataDescriptor descriptor;
    private long currentState;
    private long currentSymmetricState;

    public SortedEntitiesData(SortedEntitiesDataDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void refresh(RelativeWorldModel worldModel) {
        Point zero = Point.with(0,0);
        Ship.Relative ship = worldModel.getRelatives().stream()
                .filter(relative -> relative instanceof Ship.Relative)
                .map(relative -> (Ship.Relative)relative)
                .min(Comparator.comparing(rel -> rel.getPosition().worldDistanceTo(zero))).get();
        List<EntityRelative> relatives = worldModel.getRelatives().stream()
                .filter(rel -> rel instanceof Stone.Relative
                        && rel.getPosition().worldDistanceTo(zero) < 2 * Constants.WINDOW_HALF_WIDTH_PX / Constants.PIXELS_IN_WORLD_POINT)
                .map(rel -> {
                    if (!descriptor.isVehicleRelated()) {
                        return Pair.of(rel, rel.getPosition().worldDistanceTo(zero));
                    }
                    double cos = Math.cos(rel.getPosition().getAngle() - ship.getVehicleOrientation());
                    return Pair.of(rel, rel.getPosition().worldDistanceTo(zero) * (1.25 - cos * cos * cos));
                })
                .sorted(Comparator. comparing(Pair::second))
                .limit(descriptor.getLimit())
                .map(Pair::first)
                .collect(Collectors.toList());
        currentState = calculateDistanceSector(relatives.get(0));
        if (descriptor.isReloadRelated()) {
            currentState *= Constants.WEAPON_COOLDOWN + 1;
            currentState += ship.getEntity().getWeapon().getCooldown();
        }
        currentSymmetricState = currentState;
        Collections.reverse(relatives);
        relatives.forEach(rel -> {
            currentState = currentState * (descriptor.getAngleSectors().size() + 1) + calculateAngleSector(rel);
            currentSymmetricState = currentSymmetricState * (descriptor.getAngleSectors().size() + 1) + calculateSymmetricAngleSector(rel);
        });
    }

    public long getCurrentState() {
        return currentState;
    }

    public long getCurrentSymmetricState() {
        return currentSymmetricState;
    }

    private int calculateSymmetricAngleSector(EntityRelative relative) {
        double realAngle = -relative.getPosition().getAngle();
        long angle = (long) (descriptor.getMaxAngle() * (realAngle + (realAngle < 0 ? 2 * Math.PI : 0)) / (2 * Math.PI));
        return __calculateAngleSector(angle);
    }

    private int calculateAngleSector(EntityRelative relative) {
        double realAngle = relative.getPosition().getAngle();
        long angle = (long) (descriptor.getMaxAngle() * (realAngle + (realAngle < 0 ? 2 * Math.PI : 0)) / (2 * Math.PI));
        return __calculateAngleSector(angle);
    }

    private int __calculateAngleSector(long angle) {
        int result = 0;
        for (long border : descriptor.getAngleSectors()) {
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
        for (double border : descriptor.getDistanceSectors()) {
            if (border > distance) {
                return result;
            }
            result++;
        }
        return result;
    }
}
