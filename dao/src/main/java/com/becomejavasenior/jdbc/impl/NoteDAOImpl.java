package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.NoteDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.dbcp2.Utils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import java.util.logging.Level;
//import java.util.logging.Logger;
@Repository("noteDao")
public class NoteDAOImpl extends AbstractDAO<Note> implements NoteDAO {

    //private final static Logger logger = Logger.getLogger(CompanyDAOImpl.class.getName());

    private static final String INSERT_SQL = "INSERT INTO note (created_by_id, note, date_create, deleted," +
            " deal_id, company_id, contact_id) VALUES (?, ?, ?, FALSE, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE note SET created_by_id = ?, note = ?, date_create = ?, deleted = ?," +
            " deal_id = ?, company_id = ?, contact_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, note, created_by_id, date_create, deal_id, company_id, contact_id\n" +
            "FROM note WHERE NOT deleted";


    @Override
    public int insert(Note note) {

        if (note.getId() != 0) {
            throw new DatabaseException("note id must be obtained from DB");
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, note.getCreator().getId());
            statement.setString(2, note.getNote());
            statement.setTimestamp(3, new java.sql.Timestamp(note.getDateCreate() == null ? System.currentTimeMillis() : note.getDateCreate().getTime()));
            statement.setObject(4, note.getDeal() == null ? null : note.getDeal().getId(), Types.INTEGER);
            statement.setObject(5, note.getCompany() == null ? null : note.getCompany().getId(), Types.INTEGER);
            statement.setObject(6, note.getContact() == null ? null : note.getContact().getId(), Types.INTEGER);

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                note.setId(id);
            } else {
                throw new DatabaseException("Can't get note id from database.");
            }
            //logger.log(Level.INFO, "INSERT NEW NOTE " + note.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "note"/*, logger*/);
    }

    @Override
    public void update(Note note) {

        if (note.getId() == 0) {
            throw new DatabaseException("note must be created before update");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setInt(1, note.getCreator().getId());
            statement.setString(2, note.getNote());
            statement.setTimestamp(3, new java.sql.Timestamp(note.getDateCreate().getTime()));
            statement.setBoolean(4, note.isDelete());
            statement.setObject(5, note.getDeal() == null ? null : note.getDeal().getId(), Types.INTEGER);
            statement.setObject(6, note.getCompany() == null ? null : note.getCompany().getId(), Types.INTEGER);
            statement.setObject(7, note.getContact() == null ? null : note.getContact().getId(), Types.INTEGER);
            statement.setInt(8, note.getId());
            statement.executeUpdate();

            //logger.log(Level.INFO, "UPDATE NOTE " + note.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<Note> getAll() {

        List<Note> notes = new ArrayList<>();
        Note note;
        User creator;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            while (resultSet.next()) {

                note = new Note();
                creator = new User();

                note.setId(resultSet.getInt(FIELD_ID));
                creator.setId(resultSet.getInt("created_by_id"));
                note.setCreator(creator);
                note.setNote(resultSet.getString("note"));
                note.setDateCreate(resultSet.getTimestamp("date_create"));
                note.setDelete(false);
                if (resultSet.getObject("deal_id") != null) {
                    Deal deal = new Deal();
                    deal.setId(resultSet.getInt("deal_id"));
                    note.setDeal(deal);
                }
                if (resultSet.getObject("company_id") != null) {
                    Company company = new Company();
                    company.setId(resultSet.getInt("company_id"));
                    note.setCompany(company);
                }
                if (resultSet.getObject("contact_id") != null) {
                    Contact contact = new Contact();
                    contact.setId(resultSet.getInt("contact_id"));
                    note.setContact(contact);
                }
                notes.add(note);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return notes;
    }

    @Override
    public Note getById(int id) {

        ResultSet resultSet = null;
        Note note;
        User creator;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                note = new Note();
                creator = new User();

                note.setId(resultSet.getInt(FIELD_ID));
                creator.setId(resultSet.getInt("created_by_id"));
                note.setCreator(creator);
                note.setNote(resultSet.getString("note"));
                note.setDateCreate(resultSet.getTimestamp("date_create"));
                note.setDelete(false);
                if (resultSet.getObject("deal_id") != null) {
                    Deal deal = new Deal();
                    deal.setId(resultSet.getInt("deal_id"));
                    note.setDeal(deal);
                }
                if (resultSet.getObject("company_id") != null) {
                    Company company = new Company();
                    company.setId(resultSet.getInt("company_id"));
                    note.setCompany(company);
                }
                if (resultSet.getObject("contact_id") != null) {
                    Contact contact = new Contact();
                    contact.setId(resultSet.getInt("contact_id"));
                    note.setContact(contact);
                }
                //logger.log(Level.INFO, "GET NOTE BY ID " + note.toString());

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
        return note;
    }
}
