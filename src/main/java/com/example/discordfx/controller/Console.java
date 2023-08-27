package com.example.discordfx.controller;

import com.example.discordfx.exception.DatabaseException;
import com.example.discordfx.exception.FileException;
import com.example.discordfx.exception.ValidException;
import com.example.discordfx.service.UserService;
import com.example.discordfx.service.dto.UserDto;

import java.util.*;

public class Console{

    private UserService service = null;
    private final Scanner input = new Scanner(System.in);

    public Console() {
        try {
            this.service = new UserService();
        } catch (FileException fe) {    // Attempt on reading from a non-existing file
            System.out.println(fe.getMessage());
            System.exit(1);
        } catch (DatabaseException de) {    // Connection to the database could not be established
            System.out.println(de.getMessage());
        }
    }

    public void run() {
        showHeaderMessage();
        while (true) {
            System.out.print(">> ");
            String cmd = input.next();

            try {
                switch (cmd) {
                    case "add-user":
                        addUser();
                        break;
                    case "del-user":
                    case "rm-user":
                        removeUser();
                        break;
                    case "update-user":
                    case "modif-user":
                        updateUser();
                        break;
                    case "show-users":
                    case "get-all-users":
                        showUsers();
                        break;
                    case "get-user":
                    case "add-friend":
                    case "remove-friend":
                    case "rm-friend":
                    case "show-friends":
                    case "communities":
                    case "most-sociable":
                    case "most-sociable-community":
                        System.out.println("This feature is currently unavailable");
                        break;
                    case "exit":
                        break;
                    default:
                        System.out.println("Invalid command!");
                        break;
                }
            } catch (FileException fe) {    // The file could not be written/created
                // The most common cause is lack of empty disk space

                System.out.println(fe.getMessage());
                System.exit(1);
            } catch (DatabaseException de) {     // The connection to the database was lost
                System.out.println(de.getMessage());
                System.exit(1);
            }

            input.nextLine();
        }
    }

    private void showHeaderMessage() {
        System.out.println("Hello and welcome to the application!");
    }

    private void addUser() {
        String first, last, passwd;
        int age;

        System.out.print("First: ");
        first = input.next();
        System.out.print("Last: ");
        last = input.next();
        System.out.print("Passwd: ");
        passwd = input.next();
        System.out.print("Age: ");
        try {
            age = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid age!");
            return;
        }

        try {
            if (service.add(first, last, passwd, age)) {
                System.out.println("The user was successfully saved in memory.");
            } else {
                System.out.println("The user already exists!");
            }
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        } catch (ValidException ve) {
            System.out.print(ve.getMessage());
        }
    }

    private void removeUser() {
        int id;
        System.out.print("Id: ");
        try {
            id = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid id!");
            return;
        }

        try {
            if (service.remove(id)) {
                System.out.println("The user was successfully removed");
            } else {
                System.out.println("The user does not exist!");
            }
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        }
    }

    private void updateUser() {
        String first, last, passwd, email = "", phone = "";
        int age, id;

        System.out.print("Id: ");
        try {
            id = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid id!");
            return;
        }
        System.out.print("First: ");
        first = input.next();
        System.out.print("Last: ");
        last = input.next();
        System.out.print("Passwd: ");
        passwd = input.next();
        try {
            System.out.print("Age: ");
        } catch (InputMismatchException e) {
            System.out.println("Invalid age!");
            return;
        }
        age = input.nextInt();
        System.out.println("Do you wish to update the email for the user? (y/n)");
        if (input.next().equalsIgnoreCase("y")) {
            System.out.print("Email: ");
            email = input.next();
        }
        System.out.println("Do you wish to update the phone number of the user? (y/n)");
        if (input.next().equals("y")) {
            System.out.print("Phone: ");
            phone = input.next();
        }

        try {
            if (service.update(id, first, last, passwd, age, email, phone)) {
                System.out.println("The user was successfully updated.");
            } else {
                System.out.println("The user does not exist.");
            }
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        } catch (ValidException ve) {
            System.out.print(ve.getMessage());
        }
    }

    private void showUsers() {
        for (UserDto user : service.getAll()) {
            System.out.println(user);
        }
    }
}