package domain;

/**
 * This is a class which represent a person waiting to be served
 */
public class Person {
    private Integer desiredFloor; //represents the floor on which a person want's to go

    /**
     * Person constructor
     *
     * @param desiredFloor floor on which a person want's to go
     */
    public Person(int desiredFloor) {
        this.desiredFloor = desiredFloor;
    }

    /**
     * Getter of desired floor
     *
     * @return desired floor of the person
     */
    public int getDesiredFloor() {
        return desiredFloor;
    }
}
