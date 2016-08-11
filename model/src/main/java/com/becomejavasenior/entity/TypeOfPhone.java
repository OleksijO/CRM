package com.becomejavasenior.entity;

public enum TypeOfPhone {
    WORKING(0), WORKINGRIGHT(1), MOBILE(2), FAX(3), HOME(4), OTHER(5);

    private final int id;

    TypeOfPhone(int id) {
        this.id = id;
    }

    public static TypeOfPhone getById(int id) {
        for (TypeOfPhone typeOfPhone : TypeOfPhone.values()) {
            if (typeOfPhone.id == id)
                return typeOfPhone;
        }
        throw new IllegalArgumentException("Invalid id");
    }

    public int getId() {
        return id;
    }
}
