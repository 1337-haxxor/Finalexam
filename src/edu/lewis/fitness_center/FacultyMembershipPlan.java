package edu.lewis.fitness_center;

/**
 * Applies a 25% discount for faculty.
 */
public class FacultyMembershipPlan extends MembershipPlan {
    public FacultyMembershipPlan() {
        super("Faculty");
    }

    @Override
    public double calculateFee(double baseFee) {
        return baseFee * 0.75;
    }
}
