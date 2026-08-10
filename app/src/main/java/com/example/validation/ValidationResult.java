package com.example.validation;

public class ValidationResult {
    public enum Severity { PASS, WARNING, ERROR, CRITICAL }

    private Severity severity;
    private String message;
    private String repairSuggestion;

    public ValidationResult(Severity severity, String message, String repairSuggestion) {
        this.severity = severity;
        this.message = message;
        this.repairSuggestion = repairSuggestion;
    }

    public Severity getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getRepairSuggestion() { return repairSuggestion; }
    public boolean isPassed() { return severity == Severity.PASS || severity == Severity.WARNING; }
}
