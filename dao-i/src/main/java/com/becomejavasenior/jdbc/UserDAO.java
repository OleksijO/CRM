package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.User;

public interface UserDAO extends GenericDAO<User> {
    User getUserById(int id);
}
