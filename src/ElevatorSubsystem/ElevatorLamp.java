package ElevatorSubsystem;

public class ElevatorLamp {
    private boolean isLit;

    /**
     * Constructor for ElevatorLamp
     */
    public ElevatorLamp(){
        isLit = false;
    }

    /**
     * Set the state of the lamp
     * @param lit The state of the lamp
     */
    public void setLamp(boolean lit){
        isLit = lit;
    }

    /**
     * Getter for the state of the lamp
     * @return true is the lamp is on
     */
    public boolean isLit(){
        return isLit();
    }

}
