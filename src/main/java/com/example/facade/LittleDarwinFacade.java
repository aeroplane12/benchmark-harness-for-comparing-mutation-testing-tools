package com.example.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LittleDarwinFacade implements MutationTestingTool {
    @Override
    public void execute(String codeDir, String reportDir) {
        System.out.println("LittleDarwin wird gestartet...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java", "-jar", "libs/LittleDarwin-0.10.9.jar",
                    "--project", codeDir,
                    "--output", reportDir
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("LittleDarwin erfolgreich ausgeführt! Ergebnisse im Ordner: " + reportDir);
            } else {
                System.out.println("Fehler bei der Ausführung von LittleDarwin. Exit Code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Fehler beim Ausführen von LittleDarwin: " + e.getMessage());
        }
    }

}
