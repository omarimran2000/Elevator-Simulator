package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Destination is an object that contains the information for a floorButton request. The information is contains is
 * the floorNumber on which the floorButton was pressed and the direction with respect to the elevator
 */
public class Destination implements Serializable {
    private int floorNumber;
    private boolean isUp;

    public Destination(int floorNumber, boolean isUp) {
        this.floorNumber = floorNumber;
        this.isUp = isUp;
    }

    /**
     * Returns the isUp attribute
     *
     * @return isUp The isUp attribute
     */
    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    /**
     * Returns the floorNumber attribute
     *
     * @return floorNumber The floorNumber for the destination object
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
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
