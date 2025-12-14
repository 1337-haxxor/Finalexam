package edu.lewis.fitness_center;

/**
 * Turns whatever the user typed into the matching membership plan, falling
 * back to the community rate if it is unknown.
 */
public final class MembershipPlanFactory {
    private MembershipPlanFactory() {
    }

    public static MembershipPlan fromInput(String rawType) {
        if (rawType == null) {
            return new CommunityMembershipPlan();
        }

        String normalized = rawType.trim().toLowerCase();
        return switch (normalized) {
            case "student" -> new StudentMembershipPlan();
            case "faculty" -> new FacultyMembershipPlan();
            case "community" -> new CommunityMembershipPlan();
            default -> new CommunityMembershipPlan();
        };
    }
}
