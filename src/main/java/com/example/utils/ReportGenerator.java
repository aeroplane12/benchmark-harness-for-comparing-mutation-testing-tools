package com.example.utils;

public interface ReportGenerator {

    /**
     * Generiert einen Bericht im gewünschten Format.
     *
     * @param reportDir Verzeichnis, in dem der Bericht gespeichert wird
     */
    void generateReport(String reportDir);
}
