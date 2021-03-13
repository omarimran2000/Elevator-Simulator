package model;

import java.io.Serializable;
import java.util.Objects;

public class Destination implements Serializable {
    private final int floorNumber;
    private final boolean isUp;

    public Destination(int floorNumber, boolean isUp) {
        this.floorNumber = floorNumber;
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "floorNumber=" + floorNumber +
                ", isUp=" + isUp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destination that = (Destination) o;
        return floorNumber == that.floorNumber && isUp == that.isUp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floorNumber, isUp);
    }
}
