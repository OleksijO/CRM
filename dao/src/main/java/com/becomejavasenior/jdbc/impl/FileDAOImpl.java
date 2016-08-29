package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.FileDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.dbcp2.Utils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//import java.util.logging.Level;
//import java.util.logging.Logger;
@Repository("fileDao")
public class FileDAOImpl extends AbstractDAO<File> implements FileDAO {

    //private final static Logger logger = Logger.getLogger(CompanyDAOImpl.class.getName());

    private static final String INSERT_SQL = "INSERT INTO attached_file (created_by_id, date_create, filename, filesize, deleted," +
            " url_file, file, contact_id, company_id, deal_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE attached_file SET created_by_id = ?, date_create = ?, filename = ?, filesize = ?, deleted = ?," +
            " url_file = ?, file = ?, contact_id = ?, company_id = ?, deal_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, created_by_id, date_create, filename, filesize," +
            " url_file, file, contact_id, company_id, deal_id FROM attached_file WHERE NOT deleted";

    @Override
    public int insert(File file) {

        if (file.getId() != 0) {
            throw new DatabaseException("file id must be obtained from DB");
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, file.getCreator().getId());
            statement.setTimestamp(2, new java.sql.Timestamp(file.getDateCreate() == null ? System.currentTimeMillis() : file.getDateCreate().getTime()));
            statement.setString(3, file.getFileName());
            statement.setInt(4, file.getFileSize());
            statement.setBoolean(5, file.isDelete());
            statement.setString(6, file.getUrlFile());
            statement.setBytes(7, file.getFile());
            statement.setObject(8, file.getContact() == null ? null : file.getContact().getId(), Types.INTEGER);
            statement.setObject(9, file.getCompany() == null ? null : file.getCompany().getId(), Types.INTEGER);
            statement.setObject(10, file.getDeal() == null ? null : file.getDeal().getId(), Types.INTEGER);

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                file.setId(id);
            } else {
                throw new DatabaseException("Can't get file id from database.");
            }
            //logger.log(Level.INFO, "INSERT NEW FILE " + file.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "attached_file"/*, logger*/);
    }

    @Override
    public void update(File file) {

        if (file.getId() == 0) {
            throw new DatabaseException("file must be created before update");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setInt(1, file.getCreator().getId());
            statement.setTimestamp(2, new java.sql.Timestamp(file.getDateCreate().getTime()));
            statement.setString(3, file.getFileName());
            statement.setInt(4, file.getFileSize());
            statement.setBoolean(5, file.isDelete());
            statement.setString(6, file.getUrlFile());
            statement.setBytes(7, file.getFile());
            statement.setObject(8, file.getContact() == null ? null : file.getContact().getId(), Types.INTEGER);
            statement.setObject(9, file.getCompany() == null ? null : file.getCompany().getId(), Types.INTEGER);
            statement.setObject(10, file.getDeal() == null ? null : file.getDeal().getId(), Types.INTEGER);
            statement.setInt(11, file.getId());
            statement.executeUpdate();

            //logger.log(Level.INFO, "UPDATE FILE " + file.toString());

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<File> getAll() {

        List<File> files = new ArrayList<>();
        File file;
        User creator;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {


            while (resultSet.next()) {

                file = new File();
                creator = new User();

                file.setId(resultSet.getInt(FIELD_ID));
                creator.setId(resultSet.getInt("created_by_id"));
                file.setCreator(creator);
                file.setDateCreate(resultSet.getTimestamp("date_create"));
                file.setFileName(resultSet.getString("filename"));
                file.setFileSize(resultSet.getInt("filesize"));
                file.setDelete(false);
                file.setUrlFile(resultSet.getString("url_file"));
                file.setFile(resultSet.getBytes("file"));
                if (resultSet.getObject("contact_id") != null) {
                    Contact contact = new Contact();
                    contact.setId(resultSet.getInt("contact_id"));
                    file.setContact(contact);
                }
                if (resultSet.getObject("company_id") != null) {
                    Company company = new Company();
                    company.setId(resultSet.getInt("company_id"));
                    file.setCompany(company);
                }
                if (resultSet.getObject("deal_id") != null) {
                    Deal deal = new Deal();
                    deal.setId(resultSet.getInt("deal_id"));
                    file.setDeal(deal);
                }

                files.add(file);
            }
        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
        return files;
    }

    @Override
    public File getById(int id) {

        ResultSet resultSet = null;
        File file;
        User creator;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                file = new File();
                creator = new User();

                file.setId(resultSet.getInt(FIELD_ID));
                creator.setId(resultSet.getInt("created_by_id"));
                file.setCreator(creator);
                file.setDateCreate(resultSet.getTimestamp("date_create"));
                file.setFileName(resultSet.getString("filename"));
                file.setFileSize(resultSet.getInt("filesize"));
                file.setDelete(false);
                file.setUrlFile(resultSet.getString("url_file"));
                if (resultSet.getBytes("file")!=null){
                    file.setFile(resultSet.getBytes("file"));
                }else {
                    file.setFile(new byte[0]);
                }

                if (resultSet.getObject("contact_id") != null) {
                    Contact contact = new Contact();
                    contact.setId(resultSet.getInt("contact_id"));
                    file.setContact(contact);
                }
                if (resultSet.getObject("company_id") != null) {
                    Company company = new Company();
                    company.setId(resultSet.getInt("company_id"));
                    file.setCompany(company);
                }
                if (resultSet.getObject("deal_id") != null) {
                    Deal deal = new Deal();
                    deal.setId(resultSet.getInt("deal_id"));
                    file.setDeal(deal);
                }

                //logger.log(Level.INFO, "GET FILE BY ID " + file.toString());

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
        return file;
    }
}
