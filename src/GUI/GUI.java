package GUI;

import utill.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GUI implements GuiApi{
    private final Config config;
    private final JFrame frame;
    private Container contentPane, elevatorsContainer, floorsContainer;
    private ArrayList<ElevatorPanel> elevators;
    private ArrayList<FloorPanel> floors;
    private int numElevators, numFloors;

    //just for testing until UDP is set up
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        new GUI(config);
    }

    public GUI(Config config){
        this.config = config;
        elevators = new ArrayList<>();
        floors = new ArrayList<>();
        numElevators = config.getIntProperty("numElevators");
        numFloors = config.getIntProperty("numFloors");

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1250, 500);

        contentPane = new Container();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setVisible(true);

        elevatorsContainer = new Container();
        elevatorsContainer.setLayout(new GridLayout(0, numElevators));
        elevatorsContainer.setVisible(true);
        floorsContainer = new Container();

        frame.add(contentPane);
        contentPane.add(elevatorsContainer, BorderLayout.CENTER);
        contentPane.add(floorsContainer, BorderLayout.PAGE_END);

        for (int i = 0; i < numElevators; i++){
            ElevatorPanel e = new ElevatorPanel(numFloors, i);
            elevators.add(e);
            elevatorsContainer.add(e);
        }

        int dim = (int) Math.ceil(Math.sqrt(numFloors));
        floorsContainer.setLayout(new GridLayout(dim, dim));
        floorsContainer.setVisible(true);
        for (int i = 0; i < numFloors; i++){
            FloorPanel f = new FloorPanel(i);
            floors.add(f);
            floorsContainer.add(f);
        }
    }

    /**
     * Sets the elevator's current floor number
     * @param elevatorNumber
     * @param floorNumber
     */
    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber){
        elevators.get(elevatorNumber).setFloor(floorNumber);
    }

    /**
     * Sets the elevator's current direction of travel
     * @param elevatorNumber
     * @param direction
     */
    public void setMotorDirection(int elevatorNumber, boolean direction) {
        elevators.get(elevatorNumber).setMotorDirection(direction);
    }

    /**
     * Opens or closes the elevator doors
     * @param elevatorNumber
     * @param open
     */
    public void setDoorsOpen(int elevatorNumber, boolean open){
        elevators.get(elevatorNumber).setDoorsOpen(open);
    }

    /**
     * Sets the elevator;'s state: idle, moving, or stuck
     * @param elevatorNumber
     * @param state
     */
    public void setState(int elevatorNumber, String state){
        elevators.get(elevatorNumber).setStateText(state);
    }

    /**
     * Sets the elevator's doors stuck open or closed
     * @param elevatorNumber
     * @param doorsStuck
     */
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck, boolean open){
        elevators.get(elevatorNumber).setDoorsStuck(doorsStuck, open);
    }

    /**
     * Turns the button in the elevator corresponding to the floorNumber on or off
     * @param elevatorNumber
     * @param floorNumber
     * @param on
     */
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on){
        elevators.get(elevatorNumber).setDestination(floorNumber, on);
    }

    /**
     * Sets the up or down floor button on or off
     * @param floorNumber
     * @param direction
     * @param on
     */
    public void setFloorButton(int floorNumber, boolean direction, boolean on){
        if (direction) {
            floors.get(floorNumber).setUp(on);
        }
        else floors.get(floorNumber).setDown(on);
    }
}
