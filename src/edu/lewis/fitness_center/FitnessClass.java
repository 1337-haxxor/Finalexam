package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Models a single class on the schedule and keeps track of which members
 * grabbed a seat.
 */
public class FitnessClass {
    private final int id;
    private final String name;
    private final DifficultyLevel difficultyLevel;
    private final int capacity;
    private final List<Integer> enrolledMemberIds = new ArrayList<>();

    FitnessClass(int id, String name, DifficultyLevel difficultyLevel, int capacity) {
        this.id = id;
        this.name = name;
        this.difficultyLevel = difficultyLevel;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Integer> getEnrolledMemberIds() {
        return Collections.unmodifiableList(enrolledMemberIds);
    }

    public boolean isFull() {
        return enrolledMemberIds.size() >= capacity;
    }

    public boolean isMemberEnrolled(int memberId) {
        return enrolledMemberIds.contains(memberId);
    }

    public boolean enrollMember(int memberId) {
        if (isFull() || isMemberEnrolled(memberId)) {
            return false;
        }
        enrolledMemberIds.add(memberId);
        return true;
    }
}
