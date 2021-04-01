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

    public void setCurrentFloorNumber(int elevatorNumber, int floorNumber){
        elevators.get(elevatorNumber).setFloor(floorNumber);
    }

    public void setMotorDirection(int elevatorNumber, boolean direction) {

    }
    public void setDoorsOpen(int elevatorNumber, boolean open){

    }
    public void setState(int elevatorNumber, String state){

    }
    public void setDoorsStuck(int elevatorNumber, boolean doorsStuck){

    }
    public void setElevatorButton(int elevatorNumber, int floorNumber, boolean on){

    }
    public void setFloorButton(int floorButton, boolean direction, boolean on){

    }


}
