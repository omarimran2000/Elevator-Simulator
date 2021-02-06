package FloorSubsystem;

public class FloorLamp {

    private boolean isLit;

    /**
     * Constructor for FloorLamp
     */
    public FloorLamp(){
        isLit = false;
    }

    /**
     * @return true if the lamp is lit
     */
    public boolean isLit(){
        return isLit();
    }

    /**
     * Turn the lamp on or off
     * @param lamp The state of the lamp
     */
    public void setLamp(boolean lamp){
        isLit = lamp;
    }
}
