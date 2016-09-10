package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.ContactDAO;
import com.becomejavasenior.jdbc.TagDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Repository("contactDao")
public class ContactDAOImpl extends AbstractDAO<Contact> implements ContactDAO {

    private static final String INSERT_SQL = "INSERT INTO contact (name, responsible_users_id, pos, type_of_phone, phone," +
            " skype, email, deleted, date_create, created_by_id, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE contact SET name = ?, responsible_users_id = ?, pos = ?, type_of_phone = ?, phone = ?," +
            " skype = ?, email = ?, deleted = ?, date_create = ?, created_by_id = ?, company_id = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT contact.id, contact.name, contact.responsible_users_id, pos," +
            " type_of_phone, contact.phone, skype, contact.email, contact.date_create, contact.created_by_id, company_id," +
            " company.name AS company_name FROM contact LEFT JOIN company ON contact.company_id = company.id WHERE NOT contact.deleted";

    private static final String FIELD_RESPONSIBLE_USER_ID = "responsible_users_id";
    private static final String FIELD_POSITION = "pos";
    private static final String FIELD_TYPE_OF_PHONE = "type_of_phone";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_SKYPE = "skype";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_DATE_CREATE = "date_create";
    private static final String FIELD_CREATED_BY_ID = "created_by_id";
    private static final String FIELD_COMPANY_ID = "company_id";
    private static final String FIELD_COMPANY_NAME = "company_name";

    private TagDAO tagDAO;

    @Override
    public int insert(Contact contact) {
        if (contact.getId() != 0) {
            throw new DatabaseException("contact id must be obtained from DB");
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, contact.getName());
            statement.setObject(2, contact.getResponsibleUser() == null ? null : contact.getResponsibleUser().getId(), Types.INTEGER);
            statement.setString(3, contact.getPosition());
            statement.setObject(4, contact.getTypeOfPhone() == null ? null : contact.getTypeOfPhone().getId(), Types.INTEGER);
            statement.setString(5, contact.getPhone());
            statement.setString(6, contact.getSkype());
            statement.setString(7, contact.getEmail());
            statement.setBoolean(8, contact.isDelete());
            statement.setTimestamp(9, new java.sql.Timestamp(contact.getDateCreate() == null ? System.currentTimeMillis() : contact.getDateCreate().getTime()));
            statement.setObject(10, contact.getCreator() == null ? null : contact.getCreator().getId(), Types.INTEGER);
            statement.setObject(11, contact.getCompany() == null ? null : contact.getCompany().getId(), Types.INTEGER);

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                contact.setId(id);
            } else {
                throw new DatabaseException("Can't get contact id from database.");
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
        if (contact.getTags() != null && contact.getTags().size() > 0) {
            for (Tag tag : contact.getTags()) {
                tagDAO.insertForCompanyContact(tag, contact);
            }
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "contact");
    }

    @Override
    public void update(Contact contact) {

        if (contact.getId() == 0) {
            throw new DatabaseException("contact must be created before update (id = 0)");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, contact.getName());
            statement.setObject(2, contact.getResponsibleUser() == null ? null : contact.getResponsibleUser().getId(), Types.INTEGER);
            statement.setString(3, contact.getPosition());
            statement.setObject(4, contact.getTypeOfPhone() == null ? null : contact.getTypeOfPhone().getId(), Types.INTEGER);
            statement.setString(5, contact.getPhone());
            statement.setString(6, contact.getSkype());
            statement.setString(7, contact.getEmail());
            statement.setBoolean(8, contact.isDelete());
            statement.setTimestamp(9, new Timestamp(contact.getDateCreate().getTime()));
            statement.setInt(10, contact.getCreator().getId());
            statement.setObject(11, contact.getCompany() == null ? null : contact.getCompany().getId(), Types.INTEGER);
            statement.setInt(12, contact.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Contact> getAll() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public Contact getById(int id) {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND contact.id = ?")) {

            statement.setInt(1, id);
            List<Contact> contactList = parseResultSet(statement.executeQuery());
            return contactList == null || contactList.isEmpty() ? null : contactList.get(0);

        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    private List<Contact> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Contact> contactList = new ArrayList<>();

        while (resultSet.next()) {
            Contact contact = new Contact();
            User creator = new User();

            contact.setId(resultSet.getInt(FIELD_ID));
            contact.setName(resultSet.getString(FIELD_NAME));
            if (resultSet.getObject(FIELD_RESPONSIBLE_USER_ID, Integer.class) != null) {
                User responsibleUser = new User();
                responsibleUser.setId(resultSet.getInt(FIELD_RESPONSIBLE_USER_ID));
                contact.setResponsibleUser(responsibleUser);
            }
            contact.setPosition(resultSet.getString(FIELD_POSITION));
            contact.setTypeOfPhone(TypeOfPhone.getById(resultSet.getInt(FIELD_TYPE_OF_PHONE)));
            contact.setPhone(resultSet.getString(FIELD_PHONE));
            contact.setSkype(resultSet.getString(FIELD_SKYPE));
            contact.setEmail(resultSet.getString(FIELD_EMAIL));
            contact.setDelete(false);
            contact.setDateCreate(resultSet.getTimestamp(FIELD_DATE_CREATE));
            if (resultSet.getObject(FIELD_COMPANY_ID, Integer.class) != null) {
                Company company = new Company();
                company.setId(resultSet.getInt(FIELD_COMPANY_ID));
                company.setName(resultSet.getString(FIELD_COMPANY_NAME));
                contact.setCompany(company);
            }
            creator.setId(resultSet.getInt(FIELD_CREATED_BY_ID));
            contact.setCreator(creator);

            contactList.add(contact);
        }
        return contactList;
    }

    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }
}