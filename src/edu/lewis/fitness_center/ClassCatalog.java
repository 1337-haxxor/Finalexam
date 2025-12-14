package edu.lewis.fitness_center;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stores fitness classes and provides enrollment operations.
 */
public class ClassCatalog {
    private final Map<Integer, FitnessClass> classes = new LinkedHashMap<>();
    private int nextId = 1;

    public FitnessClass createClass(String name, DifficultyLevel difficulty, int capacity) {
        FitnessClass fitnessClass = new FitnessClass(nextId++, name, difficulty, capacity);
        classes.put(fitnessClass.getId(), fitnessClass);
        return fitnessClass;
    }

    public Collection<FitnessClass> listClasses() {
        return classes.values();
    }

    public Optional<FitnessClass> findClass(int id) {
        return Optional.ofNullable(classes.get(id));
    }
}
