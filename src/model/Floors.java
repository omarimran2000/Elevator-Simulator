package model;

import java.util.Collection;
import java.util.Set;

public class Floors extends SendSet<Integer> {
    public Floors(Collection<Integer> floors) {
        super(floors);
    }

    public Set<Integer> getFloors() {
        return items;
    }
}
