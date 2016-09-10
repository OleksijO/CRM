package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.Tag;

public interface TagDAO extends GenericDAO<Tag> {
    int insertForCompanyContact(Tag tag, Object object);
}
