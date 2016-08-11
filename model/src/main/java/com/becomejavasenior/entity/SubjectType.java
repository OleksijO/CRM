package com.becomejavasenior.entity;

public enum SubjectType {
    DEAL(0), COMPANY(1), CONTACT(2);

    private final int id;

    SubjectType(int id) {
        this.id = id;
    }

    public static SubjectType getById(int id) {
        for (SubjectType subjectType : SubjectType.values()) {
            if (subjectType.id == id)
                return subjectType;
        }
        throw new IllegalArgumentException("Invalid id");
    }

    public int getId() {
        return id;
    }
}
