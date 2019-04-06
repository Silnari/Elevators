import domain.Elevator;
import domain.Person;
import enums.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a class to control elevators
 */
class ElevatorSystem {
    private List<Elevator> elevators; //list of available elevators in the building
    private Map<Integer, List<Person>> peopleWaiting; //map of floors and list of people waiting on the specific floor

    /**
     * No-argument constructor initializes variables
     */
    ElevatorSystem() {
        elevators = new ArrayList<>();
        peopleWaiting = new HashMap<>();
    }

    /**
     * Prints status of all elevators in the building
     */
    void status() {
        for (Elevator elevator : elevators)
            System.out.println(elevator);
    }

    /**
     * Adds new person waiting to be picked up
     *
     * @param floor     represents floor where person is waiting
     * @param direction represents how many floors person wants to go
     *                  ex. -5 means that person wants to go 5 floors below
     */
    void pickup(int floor, int direction) {
        //if map does not contain floor new array list is being created
        if (!peopleWaiting.containsKey(floor))
            peopleWaiting.put(floor, new ArrayList<>());

        Person person = new Person(floor + direction);
        peopleWaiting.get(floor).add(person);

        Direction dir = direction < 0 ? Direction.DOWN : Direction.UP;
        Elevator elevator = findNearestElevatorWithSameDirection(floor, dir);

        if (elevator.getCurrentFloor() == floor) {
            elevator.addDestination(person.getDesiredFloor(), Direction.STAY);
            elevator.addPerson(person);
            peopleWaiting.get(floor).clear();
            return;
        }
        elevator.addDestination(floor, dir);
    }

    /**
     * Finds the best elevator to be sent to given floor
     *
     * @param floor     represents floor where someone is waiting
     * @param direction represents direction pressed on the calling button
     * @return a most suitable elevator to handle this person's call
     */
    private Elevator findNearestElevatorWithSameDirection(int floor, Direction direction) {
        int distance = Integer.MAX_VALUE;
        Elevator toReturn = elevators.get(0);
        for (Elevator elevator : elevators) {
            //if elevator is on called floor and it is not active - return this elevator
            if (elevator.getCurrentFloor() == floor && elevator.getDestination().isEmpty())
                return elevator;

            //if elevator has this floor on it's queue - return this elevator
            if (!elevator.getDestination().isEmpty()) {
                for (int f : elevator.getDestination()) {
                    if (f == floor)
                        return elevator;
                }
            }

            //check which elevator is nearest and has same direction
            if (Math.abs(elevator.getCurrentFloor() - floor) < distance &&
                    (elevator.getDirection() == direction || elevator.getDirection() == Direction.STAY)) {
                distance = Math.abs(elevator.getCurrentFloor() - floor);
                toReturn = elevator;
            }
        }
        return toReturn;
    }

    /**
     * Moves all elevator by one
     * and prints status of all elevator
     *
     * @see #status()
     */
    void step() {
        for (Elevator elevator : elevators)
            elevator.moveByOne(peopleWaiting);

        status();
    }

    /**
     * Adds elevator to list of elevators
     *
     * @param elevator to be add
     * @see #elevators
     */
    void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    /**
     * Shows where people are waiting
     *
     * @see #peopleWaiting
     */
    void whereArePeople() {
        for (int i = 1; i <= 10; i++) {
            if (peopleWaiting.containsKey(i) && !peopleWaiting.get(i).isEmpty()) {
                //checks whether function should print person or people
                if (peopleWaiting.get(i).size() == 1)
                    System.out.println("There is 1 person waiting on the floor " + i);
                else
                    System.out.println("There are " + peopleWaiting.get(i).size() + " people waiting on the floor " + i);
            }
        }
    }
}
