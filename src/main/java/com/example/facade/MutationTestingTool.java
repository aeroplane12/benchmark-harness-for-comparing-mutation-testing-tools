package com.example.facade;

public interface MutationTestingTool {

    /**
     * Führt das Mutation Testing aus.
     *
     * @param codeDir   Pfad zum Quellcode-Verzeichnis
     * @param reportDir Pfad zum Ergebnis-Verzeichnis
     */
    void execute(String codeDir, String reportDir);
}
