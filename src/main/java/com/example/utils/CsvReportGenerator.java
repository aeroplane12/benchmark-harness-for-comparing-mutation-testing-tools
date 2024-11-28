package com.example.utils;

import java.io.FileWriter;
import java.io.IOException;

public class CsvReportGenerator implements ReportGenerator {

    @Override
    public void generateReport(String reportDir) {
        System.out.println("Verwenden Sie die Methode generateDetailedReport für detaillierte Berichte.");
    }

    public void generateDetailedReport(String reportDir, int mutationCoverage, int killedMutations, int testStrength) {
        String csvContent = "Metric,Value\n" +
                "Mutation Coverage," + mutationCoverage + "%\n" +
                "Killed Mutations," + killedMutations + "\n" +
                "Test Strength," + testStrength + "%";

        try (FileWriter file = new FileWriter(reportDir + "/report.csv")) {
            file.write(csvContent);
            System.out.println("CSV-Bericht erfolgreich gespeichert im Ordner: " + reportDir);
        } catch (IOException e) {
            System.out.println("Fehler beim Generieren des CSV-Berichts: " + e.getMessage());
        }
    }
}
