package com.becomejavasenior.jdbc.factory;

import com.becomejavasenior.jdbc.entity.*;
import com.becomejavasenior.jdbc.impl.*;

public class PostgresDAOFactory extends AbstractDAOFactory {

   /* public static Connection getConnection() throws SQLException {
        return getConnection();
    }*/

    @Override
    public CompanyDAO getCompanyDAO() {
        return new CompanyDAOImpl();
    }

    @Override
    public ContactDAO getContactDAO() {
        return new ContactDAOImpl();
    }

    @Override
    public DealDAO getDealDAO() {
        return new DealDAOImpl();
    }

    @Override
    public FileDAO getFileDAO() {
        return new FileDAOImpl();
    }

    @Override
    public NoteDAO getNoteDAO() {
        return new NoteDAOImpl();
    }

    @Override
    public TaskDAO getTaskDAO() {
        return new TaskDAOImpl();
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl();
    }

    @Override
    public StageDAO getStageDAO() {
        return new StageDAOImpl();
    }

    @Override
    public LanguageDAO getLanguageDAO() {
        return new LanguageDAOImpl();
    }

    @Override
    public TagDAO getTagDAO() {
        return new TagDAOImpl();
    }

    @Override
    public CurrencyDAO getCurrencyDAO() {
        return new CurrencyDAOImpl();
    }

    @Override
    public RightsDAO getRightsDAO() {
        return new RightsDAOImpl();
    }

    @Override
    public VisitHistoryDAO getVisitHistoryDAO() {
        return new VisitHistoryDAOImpl();
    }
}
