package FloorSubsystem;

public class DirectionLamp {
    private boolean isLit;
    private boolean direction;

    /**
     * Constructor for DirectionLamp
     */
    public DirectionLamp(){
        isLit = false;
        direction = false;
    }

    /**
     * Set the direction of the lamp - true for up, false for down
     * @param direction The direction of the lamp
     */
    public void setDirection(boolean direction){
        this.direction = direction;
    }

    /**
     * Turn the lamp on or off
     * @param isLit The state of the lamp
     */
    public void setLit(boolean isLit){
        this.isLit = isLit;
    }

    /**
     * Getter for direction
     * @return true if direction is up
     */
    public boolean getDirection(){
        return direction;
    }

    /**
     * Getter for the state of the lamp
     * @return true if the lamp is lit
     */
    public boolean isLit(){
        return isLit;
    }
}
