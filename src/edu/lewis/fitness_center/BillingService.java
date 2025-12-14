package edu.lewis.fitness_center;

/**
 * Central place for billing math so pricing tweaks do not leak into the
 * UI.
 */
public class BillingService {
    private final double baseClassFee;

    public BillingService(double baseClassFee) {
        this.baseClassFee = baseClassFee;
    }

    public double chargeMemberForClass(Member member) {
        double amount = member.calculateClassFee(baseClassFee);
        member.addCharge(amount);
        return amount;
    }
}
