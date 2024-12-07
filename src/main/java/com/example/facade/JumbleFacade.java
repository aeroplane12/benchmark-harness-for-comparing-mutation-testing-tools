package com.example.facade;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class JumbleFacade implements MutationTestingTool {
    @Override
    public void execute(String codeDir, String reportDir) {
        System.out.println("Jumble wird gestartet...");
        try {
            String output = runJumble(codeDir, reportDir);
            if (output != null) {
                // Extract results from the output
                String results = extractResults(output);
                // Generate reports
                generateReports(reportDir, results);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Fehler beim Ausführen von Jumble: " + e.getMessage());
        }
    }

    private String runJumble(String codeDir, String reportDir) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-cp",
                "C:/Users/Keita/benchmark-harness-for-comparing-mutation-testing-tools/libs/jumble_binary_1.3.0.jar;C:/Users/Keita/benchmark-harness-for-comparing-mutation-testing-tools/target/classes",
                "com.reeltwo.jumble.Jumble",
                "com.example.Main"
        );
        processBuilder.directory(new File(codeDir)); // Set working directory
        processBuilder.redirectErrorStream(true); // Redirect errors to standard output
        Process process = processBuilder.start();

        // Capture console output
        StringBuilder consoleOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                consoleOutput.append(line).append("\n");
                System.out.println(line);  // Print output in real-time
            }
        }

        int exitCode = process.waitFor();
        return (exitCode == 0) ? consoleOutput.toString() : null;
    }

    private String extractResults(String output) {
        // Create a StringBuilder to store the results
        StringBuilder results = new StringBuilder();

        // Extract mutation score and killed mutations
        int mutationScore = extractValue(output, "Mutation Score");
        int killedMutations = extractValue(output, "Killed Mutations");

        // Add the extracted values to the results StringBuilder
        if (mutationScore != -1) {
            results.append("Mutation Score: ").append(mutationScore).append("%\n");
        }
        if (killedMutations != -1) {
            results.append("Killed Mutations: ").append(killedMutations).append("\n");
        }

        // Return the formatted results
        return results.toString();
    }

    private int extractValue(String output, String key) {
        String regex = null;

        // Define regex patterns for the various keys
        switch (key) {
            case "Mutation Score":
                regex = "Mutation Score: (\\d+)%";
                break;
            case "Killed Mutations":
                regex = "Killed Mutations: (\\d+)";
                break;
            default:
                System.out.println("Unbekannter Schlüssel: " + key);
                return -1;
        }

        if (regex != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(output);

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1)); // Extract and return the value as an integer
            }
        }
        return -1;
    }

    private void generateReports(String reportDir, String results) {
        // If no directory, use the default current directory
        if (reportDir == null || reportDir.isEmpty()) {
            reportDir = "./";
        }

        try {
            // Generate a CSV report
            File csvReportFile = new File(reportDir, "mutation_results.csv");
            try (FileWriter writer = new FileWriter(csvReportFile)) {
                writer.append("Metric,Value\n");
                writer.append("Mutation Score,").append(extractValue(results, "Mutation Score") + "%\n");
                writer.append("Killed Mutations,").append(extractValue(results, "Killed Mutations") + "").append("\n");
                System.out.println("CSV Report generated: " + csvReportFile.getAbsolutePath());
            }

            // Generate a JSON report
            File jsonReportFile = new File(reportDir, "mutation_results.json");
            try (FileWriter writer = new FileWriter(jsonReportFile)) {
                writer.append("{\n");
                writer.append("\"Mutation Score\": \"").append(extractValue(results, "Mutation Score") + "%\",\n");
                writer.append("\"Killed Mutations\": ").append(extractValue(results, "Killed Mutations")+ "").append("\n");
                writer.append("}");
                System.out.println("JSON Report generated: " + jsonReportFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.out.println("Error while generating report: " + e.getMessage());
        }
    }

}
