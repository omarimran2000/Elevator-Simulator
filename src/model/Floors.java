package model;

import java.util.Collection;
import java.util.Set;

/**
 * Class that contains the Collection of the floors
 */
public class Floors extends SendSet<Integer> {
    public Floors(Collection<Integer> floors) {
        super(floors);
    }

    public Set<Integer> getFloors() {
        return items;
    }
}
