package com.becomejavasenior.service.impl;

import com.becomejavasenior.service.CompanyService;
import com.becomejavasenior.service.ContactService;
import com.becomejavasenior.service.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {


    @Override
    public CompanyService createCompanyServiceInstance() {
        return new CompanyServiceImpl();
    }

    @Override
    public ContactService createContactServiceInstance() {
        return new ContactServiceImpl();
    }

}
