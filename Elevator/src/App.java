
/*
 * This is an program which simulates elevators.
 *
 * You can add up to 16 elevators.
 * There is 10 floors, counting from 0.
 * You can add as many people waiting on different floors as you want.
 * Elevator call buttons has the option to tell whether person wants to go up or down.
 *
 * To add new person, press 'a',
 * then press number from 0 - 10 representing floor on which person is waiting,
 * then again press number from 0 - 10 to tell on which floor the person wants to go.
 *
 * To check status of all elevators, press 'e'.
 *
 * To check where people are waiting, press 'p'.
 *
 * To step all elevators by one, press 's'.
 * All active elevators will move by one floor.
 * People are picked up automatically.
 */

/**
 * This is a main class to start program
 */
public class App {
    public static void main(String[] args) {
        ElevatorSystem system = new ElevatorSystem();
        UI ui = new UI(system);

        ui.startElevating();
    }
}
