package edu.lewis.fitness_center;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * High-level helper that wires the registries together so the console layer
 * can stay focused on user interaction.
 */
public class FitnessCenterManager {
    private final MemberRegistry memberRegistry;
    private final ClassCatalog classCatalog;
    private final TrainerDirectory trainerDirectory;
    private final BillingService billingService;

    public FitnessCenterManager(MemberRegistry memberRegistry,
            ClassCatalog classCatalog,
            TrainerDirectory trainerDirectory,
            BillingService billingService) {
        this.memberRegistry = memberRegistry;
        this.classCatalog = classCatalog;
        this.trainerDirectory = trainerDirectory;
        this.billingService = billingService;
    }

    // Member operations ----------------------------------------------------

    public Member addMember(String name, MembershipPlan plan) {
        return memberRegistry.registerMember(name, plan);
    }

    public Collection<Member> listMembers() {
        return memberRegistry.listMembers();
    }

    public Optional<Member> findMember(int id) {
        return memberRegistry.findMember(id);
    }

    public void deactivateMember(Member member) {
        member.deactivate();
    }

    public void chargeMember(Member member, double amount) {
        member.addCharge(amount);
    }

    public void recordPayment(Member member, double amount) {
        member.applyPayment(amount);
    }

    public double enrollMemberInClass(Member member, FitnessClass fitnessClass) {
        if (!member.isActive()) {
            throw new IllegalStateException("Inactive members cannot enroll.");
        }
        boolean enrolled = fitnessClass.enrollMember(member.getId());
        if (!enrolled) {
            throw new IllegalStateException("Member already enrolled or class full.");
        }
        return billingService.chargeMemberForClass(member);
    }

    // Class operations -----------------------------------------------------

    public FitnessClass createClass(String name, DifficultyLevel difficultyLevel, int capacity) {
        return classCatalog.createClass(name, difficultyLevel, capacity);
    }

    public Collection<FitnessClass> listClasses() {
        return classCatalog.listClasses();
    }

    public Optional<FitnessClass> findClass(int id) {
        return classCatalog.findClass(id);
    }

    // Trainer operations ---------------------------------------------------

    public Trainer addTrainer(String name, String specialty, List<String> availability) {
        return trainerDirectory.addTrainer(name, specialty, availability);
    }

    public Collection<Trainer> listTrainers() {
        return trainerDirectory.listTrainers();
    }

    public Optional<Trainer> findTrainer(int id) {
        return trainerDirectory.findTrainer(id);
    }
}
