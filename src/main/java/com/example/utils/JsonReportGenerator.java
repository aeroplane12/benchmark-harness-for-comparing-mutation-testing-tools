package com.example.utils;

import java.io.FileWriter;
import java.io.IOException;

public class JsonReportGenerator implements ReportGenerator{

    @Override
    public void generateReport(String reportDir) {
        System.out.println("Verwenden Sie die Methode generateDetailedReport für detaillierte Berichte.");
    }

    public void generateDetailedReport(String reportDir, int mutationCoverage, int killedMutations, int testStrength) {
        String jsonContent = String.format(
                "{\n" +
                        "  \"mutationCoverage\": \"%d%%\",\n" +
                        "  \"killedMutations\": \"%d\",\n" +
                        "  \"testStrength\": \"%d%%\"\n" +
                        "}",
                mutationCoverage, killedMutations, testStrength
        );

        try (FileWriter file = new FileWriter(reportDir + "/report.json")) {
            file.write(jsonContent);
            System.out.println("Detaillierter JSON-Bericht erfolgreich gespeichert im Ordner: " + reportDir);
        } catch (IOException e) {
            System.out.println("Fehler beim Generieren des JSON-Berichts: " + e.getMessage());
        }
    }

}
