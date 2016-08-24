package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Tag;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.apache.commons.dbcp2.Utils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

@Repository("companyDao")
public class CompanyDAOImpl extends AbstractDAO<Company> implements CompanyDAO {

    //private final static Logger logger = Logger.getLogger(CompanyDAOImpl.class.getName());

    private static final String INSERT_SQL = "INSERT INTO company (name, phone, email, address, responsible_user_id," +
            " web, deleted, created_by_id, date_create) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE company SET name = ?, phone = ?, email = ?, address = ?, responsible_user_id = ?," +
            " web = ?, deleted = ?, created_by_id = ?, date_create = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name, phone, email, address, responsible_user_id, web," +
            " created_by_id, date_create\nFROM company WHERE NOT deleted";
    private final String INSERT_COMPANY_TAG_SQL = "INSERT INTO contact_company_tag (tag_id, company_id) VALUES (?, ?)";

    @Override
    public int insert(Company company) {

        if (company.getId() != 0) {
            throw new DatabaseException("company id must be obtained from DB");
        }
        int id;

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, company.getName());
            statement.setString(2, company.getPhone());
            statement.setString(3, company.getEmail());
            statement.setString(4, company.getAddress());
            statement.setObject(5, company.getResponsibleUser() == null ? null : company.getResponsibleUser().getId(), Types.INTEGER);
            statement.setString(6, company.getWeb());
            statement.setBoolean(7, company.isDelete());
            statement.setObject(8, company.getCreator() == null ? null : company.getCreator().getId(), Types.INTEGER);
            statement.setTimestamp(9, new java.sql.Timestamp(company.getDateCreate() == null ? System.currentTimeMillis() : company.getDateCreate().getTime()));

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                company.setId(id);
            } else {
                throw new DatabaseException("Can't get company id from database.");
            }
            //logger.log(Level.INFO, "INSERT NEW COMPANY " + company.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "company"/*, logger*/);
    }

    @Override
    public void update(Company company) {

        if (company.getId() == 0) {
            throw new DatabaseException("company must be created before update");
        }
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, company.getName());
            statement.setString(2, company.getPhone());
            statement.setString(3, company.getEmail());
            statement.setString(4, company.getAddress());
            statement.setInt(5, company.getResponsibleUser().getId());
            statement.setString(6, company.getWeb());
            statement.setBoolean(7, company.isDelete());
            statement.setInt(8, company.getCreator().getId());
            statement.setTimestamp(9, new Timestamp(company.getDateCreate().getTime()));
            statement.setInt(10, company.getId());
            statement.executeUpdate();

            //logger.log(Level.INFO, "UPDATE COMPANY " + company.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Company> getAll() {

        List<Company> companies = new ArrayList<>();
        Company company;
        User responsibleUser;
        User creator;

        try (Connection connection = PostgresDAOFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            while (resultSet.next()) {

                company = new Company();
                responsibleUser = new User();
                creator = new User();

                company.setId(resultSet.getInt(FIELD_ID));
                company.setName(resultSet.getString(FIELD_NAME));
                company.setPhone(resultSet.getString("phone"));
                company.setEmail(resultSet.getString("email"));
                company.setAddress(resultSet.getString("address"));
                company.setResponsibleUser(responsibleUser);
                responsibleUser.setId(resultSet.getInt("responsible_user_id"));
                company.setWeb(resultSet.getString("web"));
                company.setDelete(false);
                company.setCreator(creator);
                creator.setId(resultSet.getInt("created_by_id"));
                company.setDateCreate(resultSet.getTimestamp("date_create"));

                companies.add(company);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return companies;
    }

    @Override
    public Company getById(int id) {

        ResultSet resultSet = null;
        Company company;
        User responsibleUser;
        User creator;

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                company = new Company();
                responsibleUser = new User();
                creator = new User();

                company.setId(resultSet.getInt(FIELD_ID));
                company.setName(resultSet.getString(FIELD_NAME));
                company.setPhone(resultSet.getString("phone"));
                company.setEmail(resultSet.getString("email"));
                company.setAddress(resultSet.getString("address"));
                company.setResponsibleUser(responsibleUser);
                responsibleUser.setId(resultSet.getInt("responsible_user_id"));
                company.setWeb(resultSet.getString("web"));
                company.setDelete(false);
                company.setCreator(creator);
                creator.setId(resultSet.getInt("created_by_id"));
                company.setDateCreate(resultSet.getTimestamp("date_create"));

                //logger.log(Level.INFO, "GET COMPANY BY ID " + company.toString());

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
        return company;
    }

    @Override
    public void companyTag(Company company, Tag tag) {

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_COMPANY_TAG_SQL)) {

            statement.setInt(1, tag.getId());
            statement.setInt(2, company.getId());
            statement.executeUpdate();
            //logger.log(Level.INFO, "INSERT NEW COMPANY " + company.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }
}
