package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.Rights;

import java.util.List;

public interface RightsDAO extends GenericDAO<Rights> {
    List<Rights> getRightsByUserId(int userId);
}
