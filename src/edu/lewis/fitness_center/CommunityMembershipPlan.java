package edu.lewis.fitness_center;

/**
 * Default membership plan with no discount.
 */
public class CommunityMembershipPlan extends MembershipPlan {
    public CommunityMembershipPlan() {
        super("Community");
    }

    @Override
    public double calculateFee(double baseFee) {
        return baseFee;
    }
}
