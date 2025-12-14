package edu.lewis.fitness_center;

/**
 * Difficulty levels for fitness classes.
 */
public enum DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    public static DifficultyLevel fromInput(String input) {
        if (input == null) {
            return BEGINNER;
        }
        return switch (input.trim().toLowerCase()) {
            case "beginner" -> BEGINNER;
            case "intermediate" -> INTERMEDIATE;
            case "advanced" -> ADVANCED;
            default -> BEGINNER;
        };
    }
}
