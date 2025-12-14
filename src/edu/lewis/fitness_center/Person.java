package edu.lewis.fitness_center;

/**
 * Simple base type for anyone we track at the fitness center so every
 * subclass gets a consistent id and name without repeating itself.
 */
public abstract class Person {
    private final int id;
    private final String name;

    protected Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (#%d)", name, id);
    }
}
