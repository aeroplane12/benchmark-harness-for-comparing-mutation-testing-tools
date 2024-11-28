package com.example.facade;

import com.example.utils.CsvReportGenerator;
import com.example.utils.JsonReportGenerator;
import com.example.utils.ReportGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PitestFacade implements MutationTestingTool{

    @Override
    public void execute(String codeDir, String reportDir) {
        System.out.println("Pitest wird gestartet...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mvn", "org.pitest:pitest-maven:mutationCoverage",
                    "-DsourceDirs=" + codeDir,
                    "-DreportDir=" + reportDir
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

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
                System.out.println("Pitest erfolgreich ausgeführt! Ergebnisse im Ordner: " + reportDir);

                // Ergebnisse analysieren
                String output = consoleOutput.toString();
                int mutationCoverage = extractValue(output, "Line Coverage");
                int killedMutations = extractValue(output, "Killed Mutations");
                int testStrength = extractValue(output, "Test Strength");

                // JSON-Bericht generieren
                ReportGenerator jsonReport = new JsonReportGenerator();
                ((JsonReportGenerator) jsonReport).generateDetailedReport(reportDir, mutationCoverage, killedMutations, testStrength);

                // CSV-Bericht generieren
                ReportGenerator csvReport = new CsvReportGenerator();
                ((CsvReportGenerator) csvReport).generateDetailedReport(reportDir, mutationCoverage, killedMutations, testStrength);
            } else {
                System.out.println("Fehler bei der Ausführung von Pitest. Exit Code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Fehler beim Ausführen von Pitest: " + e.getMessage());
        }
    }



    private int extractValue(String output, String key) {
        String regex = null;

        switch (key) {
            case "Line Coverage":
                regex = "Line Coverage.*?\\((\\d+)%\\)";
                break;
            case "Killed Mutations":
                regex = "Generated \\d+ mutations Killed (\\d+) \\(\\d+%\\)";
                break;
            case "Test Strength":
                regex = "Test strength (\\d+)%";
                break;
            default:
                System.out.println("Unbekannter Schlüssel: " + key);
                return -1;
        }

        if (regex != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(output);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return -1; // Rückgabe, wenn der Wert nicht gefunden wurde
    }





    public static void runPitest(String codeDir, String reportDir) {
        System.out.println("Pitest wird gestartet...");
        try {
            // Maven-Befehl für Pitest ausführen
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mvn", "org.pitest:pitest-maven:mutationCoverage",
                    "-DsourceDirs=" + codeDir,
                    "-DreportDir=" + reportDir
            );
            processBuilder.inheritIO(); // Konsolenausgabe weiterleiten
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Pitest erfolgreich ausgeführt! Ergebnisse im Ordner: " + reportDir);
            } else {
                System.out.println("Fehler bei der Ausführung von Pitest. Exit Code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Fehler beim Ausführen von Pitest: " + e.getMessage());
        }
    }
}
