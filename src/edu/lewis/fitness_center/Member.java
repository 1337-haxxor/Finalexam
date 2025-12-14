package edu.lewis.fitness_center;

import java.util.Objects;

/**
 * Wraps everything we care about for a member: which plan they are on,
 * whether they are active, and how much they owe.
 */
public class Member extends Person {
    private final MembershipPlan membershipPlan;
    private boolean active;
    private double balance;

    Member(int id, String name, MembershipPlan membershipPlan) {
        super(id, name);
        this.membershipPlan = Objects.requireNonNull(membershipPlan, "membershipPlan");
        this.active = true;
    }

    public MembershipPlan getMembershipPlan() {
        return membershipPlan;
    }

    public boolean isActive() {
        return active;
    }

    public double getBalance() {
        return balance;
    }

    public void deactivate() {
        this.active = false;
    }

    public void addCharge(double amount) {
        balance += amount;
    }

    public void applyPayment(double amount) {
        balance -= amount;
    }

    public double calculateClassFee(double baseFee) {
        return membershipPlan.calculateFee(baseFee);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s - balance $%.2f",
                getName(), membershipPlan.getName(), active ? "Active" : "Inactive", balance);
    }
}
