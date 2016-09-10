package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.TypeOfPhone;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.ContactDAO;
import com.becomejavasenior.jdbc.TagDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;


@Repository("contactDao")
public class ContactDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplateImpl<Contact> implements ContactDAO {

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
    private final TagDAO tagDAO;

    private static final RowMapper<Contact> ROW_MAPPER_CONTACT = (resultSet, i) -> {
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
        return contact;
    };

    @Autowired
    public ContactDaoJdbcTemplateImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public int insert(Contact contact) {

        if (contact.getId() != 0) {
            throw new DatabaseException("contact id must be obtained from DB");
        }

        PreparedStatementCreator preparedStatementCreator= connection ->{
            PreparedStatement ps =
                    connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, contact.getName());
            ps.setObject(2, contact.getResponsibleUser() == null ? null : contact.getResponsibleUser().getId(), Types.INTEGER);
            ps.setString(3, contact.getPosition());
            ps.setObject(4, contact.getTypeOfPhone() == null ? null : contact.getTypeOfPhone().getId(), Types.INTEGER);
            ps.setString(5, contact.getPhone());
            ps.setString(6, contact.getSkype());
            ps.setString(7, contact.getEmail());
            ps.setBoolean(8, contact.isDelete());
            ps.setTimestamp(9, new Timestamp(contact.getDateCreate() == null ? System.currentTimeMillis() : contact.getDateCreate().getTime()));
            ps.setObject(10, contact.getCreator() == null ? null : contact.getCreator().getId(), Types.INTEGER);
            ps.setObject(11, contact.getCompany() == null ? null : contact.getCompany().getId(), Types.INTEGER);
            return ps;
        };

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        int id=(int) keyHolder.getKey().longValue();
        contact.setId(id);
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
        PreparedStatementSetter preparedStatementSetter = statement -> {
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
            };
        jdbcTemplate.update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public List<Contact> getAll() {
        return jdbcTemplate.query(SELECT_SQL, ROW_MAPPER_CONTACT);
    }

    @Override
    public Contact getById(int id) {
        Contact contact = null;
        try {
            contact = jdbcTemplate.queryForObject(SELECT_SQL + " AND contact.id = ?", ROW_MAPPER_CONTACT, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
            // to return null if there is no object in DB with specified id. For tests.
        }
        return contact;

    }

}