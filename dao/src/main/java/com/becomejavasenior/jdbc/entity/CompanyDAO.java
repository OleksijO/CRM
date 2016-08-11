package com.becomejavasenior.jdbc.entity;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Tag;

public interface CompanyDAO extends GenericDAO<Company> {
    void companyTag(Company company, Tag tag);
}
