# Elevator Simulator

## SYSC 3303 Project 
### Milestone 2  Group 1 L5
#### By: Omar Imran, Daniel Innes, Braxton Martin, Erica Oliver, Wintana Yosief 

The Elevator system contains of three subsystems.
 
To import the project in Eclipse, unzip the archive file and then import project from file system. 

To run in the Command Prompt, first you need to type in "javac Main.java" and then "java Main" to run it.

To start the program, you have to run the Main method in the Main class. This will parse the CSV file 
containing all the events. The relative time in the program will start at 14:00:00 and will then call on events 
based on this time.

There are 4 tests included. Two are from the previous iteration (i.e. CSVTest and CommunicationTest). The new 
tests added are StateTest which tests if the states are changed properly and ElevatorMotorTest which checks 
to see if the Motor's directions work properly.

Once the scheduler thread starts, the timer will also start. Events will be scheduled based on their 
priorities (i.e. their time) so that the first event to be called will be the earlier ones. The timings 
of the requests from the floor will depend on the events. 

There will be a delay as the elevator moves from one floor to another depending on the distance and the speed 
defined in the ArrivalSensor. There will also be a delay when the doors open to when they close again to 
simulate the real world.

The floor will make a request to the scheduler once it's time to request an elevator. The scheduler
will then call on the elevator to come to the floor. The elevator will then move to the destination 
floor of the event. This will keep happening until all the events in the CSV file have been passed. 

The Elevator has three states. When it is idle, it is waiting on requests to be made. When it is moving up, 
it is going to all the floors located in the set containing destination floors that are on the way up. 
When it is moving down, it will go to all the floors located in the set containing destination floors that are 
on the way down.

The Scheudler only has two states. One where it is actively scheduling events from the Floor to the Elevator. 
Another will be when it has no events so it is idle. 

In future project iterations, the scheduler will request an elevator from the elevator subsystem as it 
will contain multiple elevators to call from. This will be done using UDP as well.
