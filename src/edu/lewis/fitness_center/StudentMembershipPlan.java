package edu.lewis.fitness_center;

/**
 * Applies a 50% discount for students.
 */
public class StudentMembershipPlan extends MembershipPlan {
    public StudentMembershipPlan() {
        super("Student");
    }

    @Override
    public double calculateFee(double baseFee) {
        return baseFee * 0.50;
    }
}
