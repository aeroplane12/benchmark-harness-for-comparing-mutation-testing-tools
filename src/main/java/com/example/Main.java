package com.example;
import com.example.facade.*;
import com.example.facade.JumbleFacade;
import com.example.facade.LittleDarwinFacade;
import com.example.facade.MutationTestingTool;
import com.example.facade.PitestFacade;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Willkommen zum Mutation Testing Framework!");

        // Benutzeroptionen anzeigen die verfügbar sind
        System.out.println("Bitte wählen Sie ein Tool aus:");
        System.out.println("1: Pitest");
        System.out.println("2: Jumble");
        System.out.println("3: LittleDarwin");

        // Eingabe des Benutzers lesen und auswerten
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();



        MutationTestingTool tool = null;

        switch (choice) {
            case 1:
                tool = new PitestFacade();
                break;
            case 2:
                tool = new JumbleFacade();
                break;
            case 3:
                tool = new LittleDarwinFacade();
                break;
            default:
                System.out.println("Ungültige Auswahl!");
                return;
        }

        if (tool != null) {
            tool.execute("C:\\Users\\Keita\\benchmark-harness-for-comparing-mutation-testing-tools\\src\\main\\java", "C:\\Users\\Keita\\benchmark-harness-for-comparing-mutation-testing-tools\\target\\pit-reports");
        }


    }

}
