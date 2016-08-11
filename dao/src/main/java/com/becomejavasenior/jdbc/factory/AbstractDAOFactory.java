package com.becomejavasenior.jdbc.factory;

import com.becomejavasenior.jdbc.entity.*;

public abstract class AbstractDAOFactory {

    public static final int POSTGRESQL = 1;

    public abstract CompanyDAO getCompanyDAO();
    public abstract ContactDAO getContactDAO();
    public abstract DealDAO getDealDAO();
    public abstract FileDAO getFileDAO();
    public abstract NoteDAO getNoteDAO();
    public abstract TaskDAO getTaskDAO();
    public abstract UserDAO getUserDAO();
    public abstract StageDAO getStageDAO();
    public abstract LanguageDAO getLanguageDAO();
    public abstract TagDAO getTagDAO();
    public abstract CurrencyDAO getCurrencyDAO();
    public abstract RightsDAO getRightsDAO();
    public abstract VisitHistoryDAO getVisitHistoryDAO();

    public static AbstractDAOFactory getDAOFactory(int factory) {
        switch (factory) {
            case POSTGRESQL:
                return new PostgresDAOFactory();
            default:
                return null;
        }
    }
}
