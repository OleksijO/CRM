package com.becomejavasenior.entity;

public enum TypeOfPeriod {
    TO_DAY(0), ALL_DAY(1), TOMMOROW(2), NEXT_WEEK(3), NEXT_MONTH(4), NEXT_YEAR(5);

    private final int id;

    TypeOfPeriod(int id) {
        this.id = id;
    }

    public static TypeOfPeriod getById(int id) {
        for (TypeOfPeriod typeOfPeriod : TypeOfPeriod.values()) {
            if (typeOfPeriod.id == id)
                return typeOfPeriod;
        }
        throw new IllegalArgumentException("Invalid id");
    }

    public int getId() {
        return id;
    }
}
