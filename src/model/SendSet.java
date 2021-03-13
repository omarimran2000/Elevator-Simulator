package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class SendSet implements Serializable {
    private final Set<Integer> floors;

    public SendSet(Set<Integer> floors) {
        this.floors = Collections.unmodifiableSet(floors);
    }

    public Set<Integer> getFloors() {
        return floors;
    }

    @Override
    public String toString() {
        return "SendSet{" +
                "floors=" + floors +
                "floors=" + floors +
                '}';

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendSet sendSet = (SendSet) o;
        return Objects.equals(floors, sendSet.floors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floors);
    }
}
