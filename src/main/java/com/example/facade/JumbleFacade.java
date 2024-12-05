package com.example.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JumbleFacade implements MutationTestingTool {
    @Override
    public void execute(String codeDir, String reportDir) {
        System.out.println("Jumble wird gestartet...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java",
                    "-cp", // Klassenpfad angeben
                    "libs/jumble_binary_1.3.0.jar;target/classes", // Jumble-JAR und kompilierten Code einbinden
                    "com.reeltwo.jumble.Jumble", // Hauptklasse von Jumble
                    "com.example.Main" // Die zu testende Klasse
            );

            processBuilder.directory(new File(".")); // Arbeitsverzeichnis setzen
            processBuilder.redirectErrorStream(true); // Fehlerumleitung zur Standardausgabe
            Process process = processBuilder.start();

            // Konsolenausgabe lesen
            StringBuilder consoleOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    consoleOutput.append(line).append("\n");
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println(" Jumble erfolgreich ausgeführt!");
                // Ergebnisse hier weiterverarbeiten, falls nötig
            } else {
                System.out.println("Fehler bei der Ausführung von Jumble. Exit Code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Fehler beim Ausführen von Jumble: " + e.getMessage());
        }
    }
}
