package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.dbcp2.Utils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.util.logging.Level;
//import java.util.logging.Logger;
//@Repository("dealDao")
public class DealDAOImpl extends AbstractDAO<Deal> implements DealDAO {

    //private final static Logger logger = Logger.getLogger(CompanyDAOImpl.class.getName());

    private static final String INSERT_SQL = "INSERT INTO deal (stage_id, responsible_users_id, company_id, created_by_id, " +
            "name, amount, deleted, date_create, primary_contact_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE deal SET stage_id = ?, responsible_users_id = ?, company_id = ?, created_by_id = ?, name = ?," +
            " amount = ?, deleted = ?, date_create = ?, primary_contact_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name, stage_id, responsible_users_id," +
            " amount, company_id, date_create, created_by_id, primary_contact_id FROM deal WHERE NOT deleted";
    private static final String INSERT_DEAL_CONTACT_SQL = "INSERT INTO deal_contact (deal_id, contact_id) VALUES (?, ?)";
    private static final String SELECT_ALL_STAGE_DEALS_SQL = "SELECT id, name FROM stage_deals WHERE deleted = FALSE";
    private static final String SELECT_ALL_DEAL_BY_STAGE = "SELECT\n" +
            "  deal.id,\n" +
            "  deal.name,\n" +
            "  deal.amount,\n" +
            "  company.id AS companyId,\n" +
            "  company.name AS companyName\n" +
            "FROM deal\n" +
            "  JOIN company ON company.id = deal.company_id\n" +
            "WHERE deal.id IN (SELECT deal.id\n" +
            "                  FROM deal\n" +
            "                    JOIN stage_deals ON deal.stage_id = stage_deals.id WHERE stage_deals.name=?)";

    private static final String SELECT_ALL_CONTACT = "SELECT contact.id, contact.name FROM deal JOIN\n" +
            "deal_contact ON\n" +
            "deal.id=deal_contact.deal_id JOIN contact ON deal_contact.contact_id=contact.id\n" +
            "WHERE deal.name=?";
    private static final String SELECT_ALL_STAGES = "SELECT id, name FROM stage_deals LIMIT 4";

    private static final String SELECT_DEALS_FOR_LIST = "SELECT\n" +
            "  deal.name,\n" +
            "  deal.amount,\n" +
            "  stage_deals.name AS stage,\n" +
            "  contact.id AS contactId,\n" +
            "  contact.name AS contact,\n" +
            "  company.id AS companyId,\n" +
            "  company.name AS company\n" +
            "FROM deal\n" +
            "  JOIN stage_deals ON deal.stage_id = stage_deals.id\n" +
            "  JOIN contact ON deal.primary_contact_id = contact.id\n" +
            "  JOIN company ON contact.company_id = company.id\n";


    @Override
    public List<Deal> getDealsForList() {
        List<Deal> deals = new ArrayList<>();
        Deal deal;
        Contact contact;
        Company company;
        Stage stage;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_DEALS_FOR_LIST)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                deal = new Deal();
                company = new Company();
                contact = new Contact();
                stage = new Stage();

                deal.setName(resultSet.getString("name"));
                deal.setAmount(resultSet.getBigDecimal("amount"));
                stage.setName(resultSet.getString("stage"));
                deal.setStage(stage);
                contact.setId(resultSet.getInt("contactId"));
                contact.setName(resultSet.getString("contact"));
                company.setId(resultSet.getInt("companyId"));
                company.setName(resultSet.getString("company"));
                contact.setCompany(company);
                deal.setPrimaryContact(contact);

                deals.add(deal);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }

        return deals;
    }

    @Override
    public List<Deal> getDealsByStage(String stage) {
        List<Deal> deals = new ArrayList<>();
        Deal deal;
        Company company;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_DEAL_BY_STAGE)) {

            statement.setString(1, stage);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                deal = new Deal();
                company = new Company();

                deal.setId(resultSet.getInt("id"));
                deal.setName(resultSet.getString("name"));
                deal.setAmount(resultSet.getBigDecimal("amount"));
                company.setId(resultSet.getInt("companyId"));
                company.setName(resultSet.getString("companyName"));
                deal.setCompany(company);

                deals.add(deal);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }

        return deals;
    }

    @Override
    public List<Contact> getContactsByDealName(String dealName) {
        List<Contact> contacts = new ArrayList<>();
        Contact contact;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CONTACT)) {

            statement.setString(1, dealName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                contact = new Contact();
                contact.setId(resultSet.getInt("id"));
                contact.setName(resultSet.getString("name"));
                contacts.add(contact);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }

        return contacts;
    }

    @Override
    public List<Stage> getAllStage() {
        List<Stage> stages = new ArrayList<>();
        Stage stage;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_STAGES)) {

            while (resultSet.next()) {

                stage = new Stage();
                stage.setId(resultSet.getInt("id"));
                stage.setName(resultSet.getString("name"));

                stages.add(stage);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return stages;
    }

    @Override
    public int insert(Deal deal) {

        if (deal.getId() != 0) {
            throw new DatabaseException("deal id must be obtained from DB");
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, deal.getStage().getId());
            statement.setObject(2, deal.getResponsibleUser() == null ? null : deal.getResponsibleUser().getId());
            statement.setInt(3, deal.getCompany().getId());
            statement.setInt(4, deal.getCreator().getId());
            statement.setString(5, deal.getName());
            statement.setBigDecimal(6, deal.getAmount());
            statement.setBoolean(7, deal.isDelete());
            statement.setTimestamp(8, new java.sql.Timestamp(deal.getDateCreate() == null ? System.currentTimeMillis() : deal.getDateCreate().getTime()));
            statement.setObject(9, deal.getPrimaryContact() == null ? null : deal.getPrimaryContact().getId(), Types.INTEGER);

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                deal.setId(id);
            } else {
                throw new DatabaseException("Can't get deal id from database.");
            }
            //logger.log(Level.INFO, "INSERT NEW DEAL " + deal.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        if (deal.getContacts() != null && deal.getContacts().size() > 0) {
            for (Contact contact : deal.getContacts()) {
                addContactToDeal(deal, contact);
            }
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "deal"/*, logger*/);
    }

    @Override
    public void update(Deal deal) {

        if (deal.getId() == 0) {
            throw new DatabaseException("deal must be created before update");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setInt(1, deal.getStage().getId());
            statement.setObject(2, deal.getResponsibleUser() == null ? null : deal.getResponsibleUser().getId());
            statement.setInt(3, deal.getCompany().getId());
            statement.setInt(4, deal.getCreator().getId());
            statement.setString(5, deal.getName());
            statement.setBigDecimal(6, deal.getAmount());
            statement.setBoolean(7, deal.isDelete());
            statement.setTimestamp(8, new java.sql.Timestamp(deal.getDateCreate().getTime()));
            statement.setObject(9, deal.getPrimaryContact() == null ? null : deal.getPrimaryContact().getId(), Types.INTEGER);
            statement.setInt(10, deal.getId());
            statement.executeUpdate();

            //logger.log(Level.INFO, "UPDATE DEAL " + deal.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Deal> getAll() {

        List<Deal> deals = new ArrayList<>();
        Deal deal;
        User responsibleUser;
        User creator;
        Company company;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            while (resultSet.next()) {

                deal = new Deal();
                responsibleUser = new User();
                creator = new User();
                company = new Company();
                Stage stage = new Stage();

                deal.setId(resultSet.getInt(FIELD_ID));
                responsibleUser.setId(resultSet.getInt("responsible_users_id"));
                deal.setResponsibleUser(responsibleUser);
                company.setId(resultSet.getInt("company_id"));
                deal.setCompany(company);
                creator.setId(resultSet.getInt("created_by_id"));
                deal.setCreator(creator);
                deal.setName(resultSet.getString(FIELD_NAME));
                deal.setAmount(resultSet.getBigDecimal("amount"));
                deal.setDelete(false);
                deal.setDateCreate(resultSet.getTimestamp("date_create"));
                stage.setId(resultSet.getInt("stage_id"));
                deal.setStage(stage);
                if (resultSet.getObject("primary_contact_id") != null) {
                    Contact primaryContact = new Contact();
                    primaryContact.setId(resultSet.getInt("primary_contact_id"));
                    deal.setPrimaryContact(primaryContact);
                }

                deals.add(deal);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return deals;
    }

    @Override
    public Deal getById(int id) {

        ResultSet resultSet = null;
        Deal deal;
        User responsibleUser;
        User creator;
        Company company;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                deal = new Deal();
                responsibleUser = new User();
                creator = new User();
                company = new Company();
                Stage stage = new Stage();

                deal.setId(resultSet.getInt(FIELD_ID));
                responsibleUser.setId(resultSet.getInt("responsible_users_id"));
                deal.setResponsibleUser(responsibleUser);
                company.setId(resultSet.getInt("company_id"));
                deal.setCompany(company);
                creator.setId(resultSet.getInt("created_by_id"));
                deal.setCreator(creator);
                deal.setName(resultSet.getString(FIELD_NAME));
                deal.setAmount(resultSet.getBigDecimal("amount"));
                deal.setDelete(false);
                deal.setDateCreate(resultSet.getTimestamp("date_create"));
                stage.setId(resultSet.getInt("stage_id"));
                deal.setStage(stage);
                if (resultSet.getObject("primary_contact_id") != null) {
                    Contact primaryContact = new Contact();
                    primaryContact.setId(resultSet.getInt("primary_contact_id"));
                    deal.setPrimaryContact(primaryContact);
                }

                //logger.log(Level.INFO, "GET DEAL BY ID " + deal.toString());

            } else {
                return null;
            }

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        } finally {
            if (resultSet != null) {
                Utils.closeQuietly(resultSet);
            }
        }
        return deal;
    }

    @Override
    public void addContactToDeal(Deal deal, Contact contact) {
        if (deal != null && deal.getId() > 0 && contact != null && contact.getId() > 0) {
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(INSERT_DEAL_CONTACT_SQL)) {
                statement.setInt(1, deal.getId());
                statement.setInt(2, contact.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseException("Can't add contact to deal relation", e);
            }
        }
    }

    @Override
    public Map<Integer, String> getStageDealsList() {

        Map<Integer, String> stageDeals = new HashMap<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_STAGE_DEALS_SQL)) {

            while (resultSet.next()) {

                stageDeals.put(resultSet.getInt("id"), resultSet.getString("name"));

            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return stageDeals;
    }

    @Override
    public Map<Company, BigDecimal> getDealAmountsForCompaniesForPeriod(java.util.Date start, java.util.Date finish) {
        //TODO implement method
        throw new RuntimeException("Operation is not supported by this implementation yet. Use another one, please.");
    }
}