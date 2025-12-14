package edu.lewis.fitness_center;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Encapsulates storage and operations for members.
 */
public class MemberRegistry {
    private final Map<Integer, Member> members = new LinkedHashMap<>();
    private int nextId = 1;

    public Member registerMember(String name, MembershipPlan plan) {
        Member member = new Member(nextId++, name, plan);
        members.put(member.getId(), member);
        return member;
    }

    public Collection<Member> listMembers() {
        return members.values();
    }

    public Optional<Member> findMember(int id) {
        return Optional.ofNullable(members.get(id));
    }
}
