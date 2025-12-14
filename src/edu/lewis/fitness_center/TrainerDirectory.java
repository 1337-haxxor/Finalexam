package edu.lewis.fitness_center;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Manages trainers and their schedules.
 */
public class TrainerDirectory {
    private final Map<Integer, Trainer> trainers = new LinkedHashMap<>();
    private int nextId = 1;

    public Trainer addTrainer(String name, String specialty, List<String> initialSchedule) {
        Trainer trainer = new Trainer(nextId++, name, specialty);
        trainer.replaceSchedule(initialSchedule);
        trainers.put(trainer.getId(), trainer);
        return trainer;
    }

    public Collection<Trainer> listTrainers() {
        return trainers.values();
    }

    public Optional<Trainer> findTrainer(int id) {
        return Optional.ofNullable(trainers.get(id));
    }
}
