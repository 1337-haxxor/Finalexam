package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console front-end that delegates work to the object-oriented domain layer.
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final double BASE_CLASS_FEE = 10.0;
    private static final FitnessCenterManager MANAGER = new FitnessCenterManager(
            new MemberRegistry(),
            new ClassCatalog(),
            new TrainerDirectory(),
            new BillingService(BASE_CLASS_FEE));

    public static void main(String[] args) {
        Main app = new Main();
        app.mainMenu();
    }

    // ---------------------------------------------------------------------
    // Utility helpers
    // ---------------------------------------------------------------------

    private void pause() {
        System.out.print("\nPress Enter to continue...");
        SCANNER.nextLine();
    }

    private int readInt(String prompt, Integer min, Integer max) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                int parsed = Integer.parseInt(value);
                if (min != null && parsed < min) {
                    System.out.println("Please enter a value >= " + min);
                    continue;
                }
                if (max != null && parsed > max) {
                    System.out.println("Please enter a value <= " + max);
                    continue;
                }
                return parsed;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt, double minValue) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                double parsed = Double.parseDouble(value);
                if (parsed < minValue) {
                    System.out.println("Please enter a value >= " + minValue);
                    continue;
                }
                return parsed;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private MembershipPlan readMembershipPlan() {
        System.out.println("Membership types: student, faculty, community");
        System.out.print("Membership type: ");
        return MembershipPlanFactory.fromInput(SCANNER.nextLine());
    }

    // ---------------------------------------------------------------------
    // Member flows
    // ---------------------------------------------------------------------

    private void addMember() {
        System.out.println("\n=== Add New Member ===");
        String name = readNonEmpty("Name: ");
        MembershipPlan plan = readMembershipPlan();
        Member member = MANAGER.addMember(name, plan);
        System.out.println("Member created: " + member);
    }

    private void listMembers(boolean showDetails) {
        System.out.println("\n=== Members ===");
        if (MANAGER.listMembers().isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        for (Member member : MANAGER.listMembers()) {
            if (showDetails) {
                System.out.printf("[%d] %s%n", member.getId(), member);
            } else {
                System.out.printf("[%d] %s%n", member.getId(), member.getName());
            }
        }
    }

    private Optional<Member> promptForMember() {
        listMembers(false);
        if (MANAGER.listMembers().isEmpty()) {
            return Optional.empty();
        }
        int id = readInt("Enter member id: ", 1, null);
        Optional<Member> member = MANAGER.findMember(id);
        if (member.isEmpty()) {
            System.out.println("Member not found.");
        }
        return member;
    }

    private void deactivateMember() {
        System.out.println("\n=== Deactivate Member ===");
        Optional<Member> member = promptForMember();
        member.ifPresent(m -> {
            if (!m.isActive()) {
                System.out.println("Member already inactive.");
                return;
            }
            MANAGER.deactivateMember(m);
            System.out.println(m.getName() + " is now inactive.");
        });
    }

    private void addChargeToMember() {
        System.out.println("\n=== Add Charge ===");
        Optional<Member> member = promptForMember();
        member.ifPresent(m -> {
            double amount = readDouble("Charge amount: $", 0.01);
            MANAGER.chargeMember(m, amount);
            System.out.printf("Recorded $%.2f charge for %s.%n", amount, m.getName());
        });
    }

    private void applyPaymentFromMember() {
        System.out.println("\n=== Record Payment ===");
        Optional<Member> member = promptForMember();
        member.ifPresent(m -> {
            double amount = readDouble("Payment amount: $", 0.01);
            MANAGER.recordPayment(m, amount);
            System.out.printf("Recorded $%.2f payment from %s.%n", amount, m.getName());
        });
    }

    // ---------------------------------------------------------------------
    // Class flows
    // ---------------------------------------------------------------------

    private void createClass() {
        System.out.println("\n=== Create Fitness Class ===");
        String name = readNonEmpty("Class name: ");
        System.out.print("Difficulty (beginner/intermediate/advanced): ");
        DifficultyLevel difficulty = DifficultyLevel.fromInput(SCANNER.nextLine());
        int capacity = readInt("Capacity: ", 1, null);
        FitnessClass fitnessClass = MANAGER.createClass(name, difficulty, capacity);
        System.out.println("Created class: " + fitnessClass.getName() + " (#" + fitnessClass.getId() + ")");
    }

    private void listClasses(boolean showDetails) {
        System.out.println("\n=== Fitness Classes ===");
        if (MANAGER.listClasses().isEmpty()) {
            System.out.println("No classes found.");
            return;
        }
        for (FitnessClass fitnessClass : MANAGER.listClasses()) {
            if (showDetails) {
                System.out.printf("[%d] %s (%s) capacity %d, enrolled %d%n",
                        fitnessClass.getId(),
                        fitnessClass.getName(),
                        fitnessClass.getDifficultyLevel(),
                        fitnessClass.getCapacity(),
                        fitnessClass.getEnrolledMemberIds().size());
            } else {
                System.out.printf("[%d] %s%n", fitnessClass.getId(), fitnessClass.getName());
            }
        }
    }

    private Optional<FitnessClass> promptForClass() {
        listClasses(false);
        if (MANAGER.listClasses().isEmpty()) {
            return Optional.empty();
        }
        int id = readInt("Enter class id: ", 1, null);
        Optional<FitnessClass> target = MANAGER.findClass(id);
        if (target.isEmpty()) {
            System.out.println("Class not found.");
        }
        return target;
    }

    private void enrollMemberInClass() {
        System.out.println("\n=== Enroll Member in Class ===");
        Optional<Member> member = promptForMember();
        if (member.isEmpty()) {
            return;
        }
        Optional<FitnessClass> fitnessClass = promptForClass();
        if (fitnessClass.isEmpty()) {
            return;
        }

        try {
            double amount = MANAGER.enrollMemberInClass(member.get(), fitnessClass.get());
            System.out.printf("Enrolled %s and charged $%.2f%n", member.get().getName(), amount);
        } catch (IllegalStateException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }

    private void listClassRoster() {
        System.out.println("\n=== Class Roster ===");
        Optional<FitnessClass> fitnessClass = promptForClass();
        if (fitnessClass.isEmpty()) {
            return;
        }
        List<Integer> roster = fitnessClass.get().getEnrolledMemberIds();
        if (roster.isEmpty()) {
            System.out.println("No members enrolled.");
            return;
        }
        System.out.println("Roster for " + fitnessClass.get().getName() + ":");
        for (Integer memberId : roster) {
            Optional<Member> member = MANAGER.findMember(memberId);
            member.ifPresent(
                    m -> System.out.println("- " + m.getName() + " (" + m.getMembershipPlan().getName() + ")"));
        }
    }

    // ---------------------------------------------------------------------
    // Trainer flows
    // ---------------------------------------------------------------------

    private void addTrainer() {
        System.out.println("\n=== Add Trainer ===");
        String name = readNonEmpty("Trainer name: ");
        String specialty = readNonEmpty("Specialty: ");
        List<String> schedule = readSchedule();
        Trainer trainer = MANAGER.addTrainer(name, specialty, schedule);
        System.out.println("Trainer created: " + trainer.getName() + " (#" + trainer.getId() + ")");
    }

    private void listTrainers(boolean showSchedule) {
        System.out.println("\n=== Trainers ===");
        if (MANAGER.listTrainers().isEmpty()) {
            System.out.println("No trainers found.");
            return;
        }
        for (Trainer trainer : MANAGER.listTrainers()) {
            if (showSchedule) {
                String schedule = trainer.getSchedule().isEmpty()
                        ? "No availability"
                        : String.join(", ", trainer.getSchedule());
                System.out.printf("[%d] %s - %s | %s%n",
                        trainer.getId(), trainer.getName(), trainer.getSpecialty(), schedule);
            } else {
                System.out.printf("[%d] %s%n", trainer.getId(), trainer.getName());
            }
        }
    }

    private Optional<Trainer> promptForTrainer() {
        listTrainers(false);
        if (MANAGER.listTrainers().isEmpty()) {
            return Optional.empty();
        }
        int id = readInt("Enter trainer id: ", 1, null);
        Optional<Trainer> trainer = MANAGER.findTrainer(id);
        if (trainer.isEmpty()) {
            System.out.println("Trainer not found.");
        }
        return trainer;
    }

    private List<String> readSchedule() {
        List<String> schedule = new ArrayList<>();
        System.out.println("Enter availability (blank line to stop). Examples: Mon 9-11");
        while (true) {
            System.out.print("Availability: ");
            String slot = SCANNER.nextLine().trim();
            if (slot.isEmpty()) {
                break;
            }
            schedule.add(slot);
        }
        return schedule;
    }

    private void updateTrainerSchedule() {
        System.out.println("\n=== Update Trainer Schedule ===");
        Optional<Trainer> trainerOpt = promptForTrainer();
        trainerOpt.ifPresent(trainer -> {
            System.out.println("Current schedule: " + (trainer.getSchedule().isEmpty()
                    ? "No availability"
                    : String.join(", ", trainer.getSchedule())));
            System.out.println("1. Replace schedule");
            System.out.println("2. Add to schedule");
            int choice = readInt("Choice: ", 1, 2);
            if (choice == 1) {
                trainer.replaceSchedule(readSchedule());
            } else {
                List<String> additions = readSchedule();
                additions.forEach(trainer::addSlot);
            }
            System.out.println("Updated schedule: " + (trainer.getSchedule().isEmpty()
                    ? "No availability"
                    : String.join(", ", trainer.getSchedule())));
        });
    }

    // ---------------------------------------------------------------------
    // Reporting
    // ---------------------------------------------------------------------

    private void showSummaryReport() {
        System.out.println("\n=== Summary Report ===");
        int totalMembers = MANAGER.listMembers().size();
        long activeMembers = MANAGER.listMembers().stream().filter(Member::isActive).count();
        double totalBalance = MANAGER.listMembers().stream().mapToDouble(Member::getBalance).sum();
        System.out.println("Total members: " + totalMembers);
        System.out.println("Active members: " + activeMembers);
        System.out.printf("Total outstanding balance: $%.2f%n", totalBalance);

        System.out.println("\nClasses:");
        if (MANAGER.listClasses().isEmpty()) {
            System.out.println("- none -");
        } else {
            for (FitnessClass fitnessClass : MANAGER.listClasses()) {
                System.out.printf("- %s (%s) %d/%d enrolled%n",
                        fitnessClass.getName(),
                        fitnessClass.getDifficultyLevel(),
                        fitnessClass.getEnrolledMemberIds().size(),
                        fitnessClass.getCapacity());
            }
        }

        System.out.println("\nTrainers:");
        if (MANAGER.listTrainers().isEmpty()) {
            System.out.println("- none -");
        } else {
            for (Trainer trainer : MANAGER.listTrainers()) {
                System.out.printf("- %s (%s)%n", trainer.getName(), trainer.getSpecialty());
            }
        }
    }

    // ---------------------------------------------------------------------
    // Menus
    // ---------------------------------------------------------------------

    private void memberMenu() {
        while (true) {
            System.out.println("\n=== Member Menu ===");
            System.out.println("1. Add member");
            System.out.println("2. List members");
            System.out.println("3. Deactivate member");
            System.out.println("4. Add charge to member");
            System.out.println("5. Record payment");
            System.out.println("6. Back to main menu");
            int choice = readInt("Choice: ", 1, 6);
            switch (choice) {
                case 1 -> addMember();
                case 2 -> listMembers(true);
                case 3 -> deactivateMember();
                case 4 -> addChargeToMember();
                case 5 -> applyPaymentFromMember();
                case 6 -> {
                    return;
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
            pause();
        }
    }

    private void classesMenu() {
        while (true) {
            System.out.println("\n=== Classes Menu ===");
            System.out.println("1. Create class");
            System.out.println("2. List classes");
            System.out.println("3. Enroll member");
            System.out.println("4. Show class roster");
            System.out.println("5. Back to main menu");
            int choice = readInt("Choice: ", 1, 5);
            switch (choice) {
                case 1 -> createClass();
                case 2 -> listClasses(true);
                case 3 -> enrollMemberInClass();
                case 4 -> listClassRoster();
                case 5 -> {
                    return;
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
            pause();
        }
    }

    private void trainerMenu() {
        while (true) {
            System.out.println("\n=== Trainer Menu ===");
            System.out.println("1. Add trainer");
            System.out.println("2. List trainers");
            System.out.println("3. Update schedule");
            System.out.println("4. Back to main menu");
            int choice = readInt("Choice: ", 1, 4);
            switch (choice) {
                case 1 -> addTrainer();
                case 2 -> listTrainers(true);
                case 3 -> updateTrainerSchedule();
                case 4 -> {
                    return;
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
            pause();
        }
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== Campus Fitness Center Management ===");
            System.out.println("1. Manage members");
            System.out.println("2. Manage classes");
            System.out.println("3. Manage trainers");
            System.out.println("4. Show summary report");
            System.out.println("5. Exit");
            int choice = readInt("Choice: ", 1, 5);
            switch (choice) {
                case 1 -> memberMenu();
                case 2 -> classesMenu();
                case 3 -> trainerMenu();
                case 4 -> {
                    showSummaryReport();
                    pause();
                }
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
