package com.becomejavasenior.service;

public interface ServiceFactory {

    CompanyService createCompanyServiceInstance();

    ContactService createContactServiceInstance();
}
