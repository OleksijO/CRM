package com.becomejavasenior.jdbc.entity;

import com.becomejavasenior.entity.User;

public interface UserDAO extends GenericDAO<User> {
    User getUserById(int id);
}
