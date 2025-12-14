package edu.lewis.fitness_center;

/**
 * Abstraction for membership pricing strategies.
 */
public abstract class MembershipPlan {
    private final String name;

    protected MembershipPlan(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Calculates the fee for a class based on the membership rules.
     */
    public abstract double calculateFee(double baseFee);
}
