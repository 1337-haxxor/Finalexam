package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Captures a trainer's specialty and the time slots they can cover so we
 * can schedule classes without juggling loose strings.
 */
public class Trainer extends Person {
    private final String specialty;
    private final List<String> schedule = new ArrayList<>();

    Trainer(int id, String name, String specialty) {
        super(id, name);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public List<String> getSchedule() {
        return Collections.unmodifiableList(schedule);
    }

    public void replaceSchedule(List<String> slots) {
        schedule.clear();
        if (slots != null) {
            schedule.addAll(slots);
        }
    }

    public void addSlot(String slot) {
        if (slot != null && !slot.isBlank()) {
            schedule.add(slot);
        }
    }
}
