package com.becomejavasenior.jdbc;

import java.math.BigDecimal;

public interface DashboardDAO {

    int getNumberOfDeals();

    BigDecimal getTotalBudgetAmount();

    int getNumberOfSuccessfulDeals();

    int getNumberOfFailedDeals();

    int getNumberOfTaskLessDeals();

    int getNumberOfDealsWithTasks();

    int getNumberOfCurrentTasks();

    int getNumberOfCompleatedTasks();

    int getNumberOfTermOutTasks();

    int getTotalNumberOfContacts();

    int getTotalNumberOfCompanies();

}
