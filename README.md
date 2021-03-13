# Elevator Simulator

## SYSC 3303 Project

### Milestone 3  Group 1 L5

#### By: Omar Imran, Daniel Innes, Braxton Martin, Erica Oliver, Wintana Yosief

The Elevator system contains of three subsystems.

To import the project in Eclipse, unzip the archive file and then import project from file system.

To run in the Command Prompt, first you need to run the ElevatorSubsystem and Scheduler. Then run the FloorSubsystem.

The FloorSubsystem will parse the CSV file containing all the
events. The relative time in the program will start at 14:00:00 and will then call on events based on this time.

The Floors will send UDP requests to the Scheduler which will find the best elevator and send the request to it. 
In this, the Floors are the Clients, Scheduler is the Host and the Elevators are the servers. The elevator will then move to the destination floor of the event. 
This will keep happening until all the events in the CSV file have been passed.

There are 6 tests included, 5 are from previous iterations (i.e. CSVTest, CommunicationTest, StateTest, ElevatorMotorTest, FloorsTest). The new test
added is StubTest which tests the sendAndReceive and interrupt methods as well as the socket timeout.

Once the scheduler thread starts, the timer will also start. Events will be scheduled based on their priorities (i.e.
their time) so that the first event to be called will be the earlier ones. The timings of the requests from the floor
will depend on the events.

There will be a delay as the elevator moves from one floor to another depending on the distance and the speed defined in
the ArrivalSensor. There will also be a delay when the doors open to when they close again to simulate the real world.

The Elevator has three states. When it is idle, it is waiting on requests to be made. When it is moving up, it is going
to all the floors located in the set containing destination floors that are on the way up. When it is moving down, it
will go to all the floors located in the set containing destination floors that are on the way down.

The Scheduler only has two states. One where it is actively scheduling events from the Floor to the Elevator. Another
will be when it has no events so it is idle.

In future project iterations, there will be faults introduced to see if the system can recover. 

## Submitted Files 
* ElevatorSubsystem Package: has all the files containing the ElevatorSubSystem 
    * ArrivalSensor.java: represents the ArrivalSensor used to check if there is a request on the floor
    * Door.java: represents the ElevatorDoor 
    * Elevator.java: represents a single Elevator 
    * ElevatorApi.java: interface implemented by Elevator.java
    * ElevatorButton.java: represents the buttons inside the elevators
    * ElevatorLamp.java: represents the lamps that turn on when an ElevatorButton is pressed
    * ElevatorSubsystem.java: generates all necessary elevators for the system
    * Motor.java: represents the motors that move the elevators
    
* FloorSubsystem Package: has all the files containing the FloorSubsystem
    * Floor.java: represents a floor in the building
    * BottomFloor.java: is a floor with only an up button
    * MiddleFloor.java: is a floor with both up and down buttons
    * TopFloor.java: is a floor with only a down button
    * FloorApi.java: interface implemented by Floor.java
    * FloorButton.java: represents the buttons on the floors to request elevators
    * FloorLamp.java: represents the lamps that turn on when a FloorButton is pressed
    * FloorSubsystem.java: generates all necessary floors for the system
    
* model Package:
    * AckMessage.java: Acknowledgment message to be used in stub as the return for void functions
    * Event.java: represents the events generated by users in the form (date, currentFloor, direction, destinationFloor)
    * StubRequestMessage.java: Representation of a function call to be used in stub
    
* SchedulerSubsystem Package:
    * Scheduler.java: schedules the events
    * SchedulerApi.java: interface implemented by Scheduler.java
    
* stub Package:
    * StubClient: Abstract stub client for remote procedure calls
    * StubServer: Abstract stub server for remote procedure calls

* helloWorldStub Package:
    * HelloWorld.java: a simple hello world class used for testing the stubs
    * HelloWorldApi.java: interface implemented by HelloWorld.java
    * HelloWorldClient.java: an example implementation of StubClient used for testing
    * HelloWorldServer.java: an example implementation of StubServer used for testing

* tests: 
    * CommunicationTest.java: Testing the communication between systems
    * CSVTest.java: Testing the reading of the input csv file
    * ElevatorMotorTest.java: Testing the initialization of the motor
    * FloorsTest.java: Testing the floors generation
    * StateTest.java: Testing the states
    * StubTest.java: Testing the stubs and remote procedure calls
    
* Main.java: the main class that starts the threads
* test.csv: the input file that simulates the events
* config.cfg: the configuration file which holds the number of floors and elevators, the elevator velocity, etc.

