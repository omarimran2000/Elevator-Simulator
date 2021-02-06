package ElevatorSubsystem;

/**
 * The Elevator Button class represents the buttons inside
 * the elevator used to request a destination floor
 * @version Feb 06, 2021
 */
public class ElevatorButton {
    private int floor;

    /**
     * Constructor for ElevatorButton
     * @param floor The floor associated with the button
     */
    public ElevatorButton(int floor){
        this.floor = floor;
    }

    /**
     * Getter for floor number
     * @return The floor number associated with the button
     */
    public int getFloor(){
        return floor;
    }
}
