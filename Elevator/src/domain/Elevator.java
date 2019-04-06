package domain;

import enums.Direction;

import java.util.*;

/**
 * This is a class which represent an elevator
 */
public class Elevator {
    private int ID; //elevator's unique ID
    private Integer currentFloor; //current floor of the elevator
    private int peopleInside; //how many people is inside of elevator
    private Map<Integer, List<Person>> people; //map of people inside and their destinations
    private LinkedList<Integer> destinations; //list of destinations of elevator
    private Direction direction; //direction of the elevator

    /**
     * Elevator constructor initializes variables
     *
     * @param ID unique number of elevator
     */
    public Elevator(int ID) {
        this.ID = ID;
        currentFloor = 0;
        peopleInside = 0;
        people = new HashMap<>();
        destinations = new LinkedList<>();
        direction = Direction.STAY;
    }


    /**
     * Getter of current floor
     *
     * @return current floor of the elevator
     */
    public int getCurrentFloor() {
        return currentFloor;
    }


    /**
     * Getter of destinations
     *
     * @return destinations of the elevator
     */
    public LinkedList<Integer> getDestination() {
        return destinations;
    }


    /**
     * Adds new destination to list of destinations.
     *
     * @param destination to be added the to list of destinations
     * @param dir         - direction describing where person wants to go
     *                    ex: if person is on 4th floor and he wants to go to 10th floor: direction will be UP,
     *                    but if person is in the elevator and he wants to go to somewhere direction will be STAY
     * @see #addInRightSpot(int, Direction)
     * This is not simple FCFS (first come fist serve) system, it tries to add new destination in the best place
     * in the list of destinations rather than adding new request to the end of the list
     */
    public void addDestination(int destination, Direction dir) {
        //if elevator already has given destination in the list of destinations, do nothing
        if (destinations.contains(destination)) {
            return;
        }

        //if elevator is not busy at all, add destination to queue and set direction to destined floor
        if (destinations.isEmpty()) {
            destinations.add(destination);
            direction = destination - currentFloor > 0 ? Direction.UP : Direction.DOWN;
            return;
        }

        //if elevator is going up, pick everything on its way (if direction is same as elevator or STAY)
        if (direction == Direction.UP) { //destinations.get(0) > destination &&
            if (destination > currentFloor && (dir == Direction.UP || dir == Direction.STAY)) {
                addInRightSpot(destination, dir);
                return;
            }
        }

        //if elevator is going down, pick everything on its way (if direction is same as elevator or STAY)
        if (direction == Direction.DOWN) {
            if (destination < currentFloor && (dir == Direction.DOWN || dir == Direction.STAY)) {
                addInRightSpot(destination, dir);
                return;
            }
        }

        //if none of above is matched, add destination to the end of the list
        destinations.addLast(destination);

        //set direction to destined floor
        direction = destinations.get(0) - currentFloor > 0 ? Direction.UP : Direction.DOWN;
    }

    /**
     * Adds given destination to the list of destinations
     *
     * @param floor to be added to the list of destinations
     * @param dir   - direction describing where person wants to go
     *              same explanation as in method addDirection
     * @see #findRightSpot(int, Direction)
     */
    private void addInRightSpot(int floor, Direction dir) {
        if (destinations.size() >= 2) destinations.add(findRightSpot(floor, dir), floor);
        else {
            if (dir == Direction.UP || (dir == Direction.STAY && direction == Direction.UP))
                destinations.add(destinations.get(0) > floor ? 0 : 1, floor);
            else
                destinations.add(destinations.get(0) < floor ? 0 : 1, floor);
        }
    }

    /**
     * Finds best place in list of destinations to put new destination
     *
     * @param floor to be added to the list of destinations
     * @param dir   direction describing where person wants to go
     *              same explanation as in method addDirection
     * @return index describing where put new destination
     */
    private int findRightSpot(int floor, Direction dir) {
        for (int i = 0; i < destinations.size() - 1; i++) {
            //if direction of the elevator (UP) matches direction of the person (UP or STAY)
            if (dir == Direction.UP || (dir == Direction.STAY && direction == Direction.UP)) {
                //if i destination on the list is higher than given destination return its index
                if (destinations.get(i) > floor)
                    return i;

                //if after i destination elevator will go down, return i + 1 index (adding destination before elevator will go down)
                //if i + 1 destination on the list is higher than given destination return its index
                if (destinations.get(i) > destinations.get(i + 1) || destinations.get(i + 1) > floor)
                    return i + 1;
            } else { //if direction of the elevator (DOWN) matches direction of the person (DOWN or STAY)
                //if i destination on the list is lower than given destination return its index
                if (destinations.get(i) < floor)
                    return i;

                //if after i destination elevator will go up, return i + 1 index (adding destination before elevator will go up)
                //if i + 1 destination on the list is lower than given destination return its index
                if (destinations.get(i) < destinations.get(i + 1) || destinations.get(i + 1) < floor)
                    return i + 1;
            }
        }
        //if none of above is matched return last index
        return destinations.size();
    }

    /**
     * Adds a person to the list of people
     *
     * @param person to be add
     */
    public void addPerson(Person person) {
        if (!people.containsKey(person.getDesiredFloor()))
            people.put(person.getDesiredFloor(), new ArrayList<>());

        people.get(person.getDesiredFloor()).add(person);
        peopleInside++;
    }

    /**
     * Removes people from the floor
     *
     * @param floor on which people want to leave
     */
    private void removePeople(int floor) {
        if (people.containsKey(floor)) {
            peopleInside -= people.get(floor).size();
            people.get(floor).clear();
        }
    }

    /**
     * Getter of direction
     *
     * @return direction of the elevator
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Moves elevator by one step, elevator picks people from this floor,
     * and leave people if they want to leave
     *
     * @param peopleWaiting list of people waiting to be served
     */
    public void moveByOne(Map<Integer, List<Person>> peopleWaiting) {
        //if elevator is not active - do nothing
        if (direction == Direction.STAY)
            return;

        //if elevator is heading up - go up by one, else go down by one
        if (direction == Direction.UP)
            currentFloor++;
        else
            currentFloor--;

        //if current floor is on the list of destinations and there are people waiting here, pick them
        if (!destinations.isEmpty() && currentFloor.equals(destinations.get(0)) && peopleWaiting.containsKey(currentFloor)) { //destinations.contains(currentFloor)
            //handle single person at once
            for (Person person : peopleWaiting.get(currentFloor)) {
                addPerson(person);
                destinations.remove(currentFloor);
                addDestination(person.getDesiredFloor(), Direction.STAY);
            }
            peopleWaiting.get(currentFloor).clear();
        }

        //if someone wants to leave on this floor, go ahead and leave
        if (people.get(currentFloor) != null && !people.get(currentFloor).isEmpty()) {
            removePeople(currentFloor);
            destinations.remove(currentFloor);
        }

        //prevents elevators from going crazy - if elevator reached destination but there is no one to be picked, remove this destination
        if (!destinations.isEmpty() && destinations.get(0).equals(currentFloor) && peopleWaiting.get(currentFloor).isEmpty()) {
            destinations.remove(0);
        }

        //if there is no more floors to be visit - stay and do nothing
        if (destinations.isEmpty()) {
            direction = Direction.STAY;
            return;
        }

        //set direction to next destination
        direction = destinations.get(0) - currentFloor > 0 ? Direction.UP : Direction.DOWN;
    }

    /**
     * Prints id, current floor, destinations and people inside
     *
     * @return string which has all of above
     */
    @Override
    public String toString() {
        return "ID: " + ID + ", current floor: " + currentFloor + ", destination: " + destinations + ", people inside: " + peopleInside;
    }
}
