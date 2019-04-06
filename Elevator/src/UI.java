import domain.Elevator;

import java.util.Scanner;

/**
 * This is a class which represent an user interface
 */
class UI {
    private Scanner scanner; //scanner to read user's input
    private ElevatorSystem system; //system to control elevators

    /**
     * UI constructor initializes instance variable
     *
     * @param system is a system which control elevators
     */
    UI(ElevatorSystem system) {
        scanner = new Scanner(System.in);
        this.system = system;
    }

    /**
     * Prints all available options and reads user input
     */
    void startElevating() {
        System.out.println("How many elevators?");
        int numberOfElevators;
        //checking if number is higher than 0 and lower than 16
        while (true) {
            try {
                numberOfElevators = Integer.parseInt(scanner.nextLine());

                if (numberOfElevators < 1 || numberOfElevators > 16) {
                    System.out.println("Number of elevators must be between 1 - 16!\n" +
                            "How many elevators?");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Type a number!\n" +
                        "How many elevators?");
                continue;
            }

            break;
        }

        //adding all elevators to the system
        for (int i = 0; i < numberOfElevators; i++) {
            system.addElevator(new Elevator(i + 1));
        }
        //printing the menu
        while (true) {
            System.out.println("\nPress:\n" +
                    "[a] - adding new person\n" +
                    "[s] - step all elevators\n" +
                    "[e] - print status of elevators\n" +
                    "[p] - show where people are waiting\n" +
                    "[q] - quit");

            String response = scanner.nextLine();

            if (response.equals("a")) {
                System.out.println("On which floor: ");
                int floor;
                //checking if floor is lower than 11 and higher than -1
                while (true) {
                    try {
                        floor = Integer.parseInt(scanner.nextLine());

                        if (floor < 0 || floor > 10) {
                            System.out.println("Choose floor from 0 to 10!\n" +
                                    "On which floor?");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Type a number!\n" +
                                "On which floor?");
                        continue;
                    }

                    break;
                }
                System.out.println("Where to go: ");
                int dir;
                //checking if destination is lower than 11 and higher than -1
                while (true) {
                    try {
                        dir = Integer.parseInt(scanner.nextLine());

                        if (dir < 0 || dir > 10) {
                            System.out.println("You can't go upper than 10 or lower than 0!\n" +
                                    "Where to go: ");
                            continue;
                        }

                        if (dir == floor) {
                            System.out.println("Don't call the elevator unless you want to go somewhere!\n" +
                                    "Where to go: ");
                            continue;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Type a number!\n" +
                                "Where to go: ");
                        continue;
                    }

                    break;
                }
                system.pickup(floor, dir - floor);
                continue;
            }
            if (response.equals("s")) {
                system.step();
                continue;
            }
            if (response.equals("e")) {
                system.status();
                continue;
            }
            if (response.equals("p")) {
                system.whereArePeople();
                continue;
            }
            if (response.equals("q")) {
                System.out.println("Closing...");
                break;
            }

            System.out.println("Wrong option, please choose one from options below:");
        }
    }
}
