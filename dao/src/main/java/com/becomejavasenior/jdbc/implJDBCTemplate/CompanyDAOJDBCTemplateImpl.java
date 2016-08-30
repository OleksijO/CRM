package com.becomejavasenior.jdbc.implJDBCTemplate;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Tag;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;

//@Repository("companyDaoJdbcTemplate")
public class CompanyDAOJDBCTemplateImpl extends AbstractDAOJDBCTemplate<Company> implements CompanyDAO {

    //private final static Logger logger = Logger.getLogger(CompanyDAOImpl.class.getName());

    private static final String INSERT_SQL = "INSERT INTO company (name, phone, email, address, responsible_user_id," +
            " web, deleted, created_by_id, date_create) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE company SET name = ?, phone = ?, email = ?, address = ?, responsible_user_id = ?," +
            " web = ?, deleted = ?, created_by_id = ?, date_create = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name, phone, email, address, responsible_user_id, web," +
            " created_by_id, date_create\nFROM company WHERE NOT deleted";
    private final String INSERT_COMPANY_TAG_SQL = "INSERT INTO contact_company_tag (tag_id, company_id) VALUES (?, ?)";

    private final RowMapper<Company> ROW_MAPPER_COMPANY = (resultSet, i) -> {
        Company company = new Company();
        User responsibleUser = new User();
        User creator = new User();

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
        return company;
    };

    @Override
    public int insert(Company company) {

        if (company.getId() != 0) {
            throw new DatabaseException("company id must be obtained from DB");
        }
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setString(1, company.getName());
            ps.setString(2, company.getPhone());
            ps.setString(3, company.getEmail());
            ps.setString(4, company.getAddress());
            ps.setObject(5, company.getResponsibleUser() == null ? null : company.getResponsibleUser().getId(), Types.INTEGER);
            ps.setString(6, company.getWeb());
            ps.setBoolean(7, company.isDelete());
            ps.setObject(8, company.getCreator() == null ? null : company.getCreator().getId(), Types.INTEGER);
            ps.setTimestamp(9, new Timestamp(company.getDateCreate() == null ? System.currentTimeMillis() : company.getDateCreate().getTime()));
            return ps;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        int id=(int) keyHolder.getKey().longValue();
        company.setId(id);
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
        PreparedStatementSetter preparedStatementSetter = ps -> {
            ps.setString(1, company.getName());
            ps.setString(2, company.getPhone());
            ps.setString(3, company.getEmail());
            ps.setString(4, company.getAddress());
            ps.setInt(5, company.getResponsibleUser().getId());
            ps.setString(6, company.getWeb());
            ps.setBoolean(7, company.isDelete());
            ps.setInt(8, company.getCreator().getId());
            ps.setTimestamp(9, new Timestamp(company.getDateCreate().getTime()));
            ps.setInt(10, company.getId());
        };
        jdbcTemplate.update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public List<Company> getAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, ROW_MAPPER_COMPANY);
    }

    @Override
    public Company getById(int id) {
        Company company = null;
        try {
            company = jdbcTemplate.queryForObject(SELECT_ALL_SQL + " AND id = ?", ROW_MAPPER_COMPANY, id);
        } catch (EmptyResultDataAccessException ignored) {
            // to return null if there is no object in DB with specified id. For tests.
            return null;
        }
        return company;
    }

    @Override
    public void companyTag(Company company, Tag tag) {
        PreparedStatementSetter preparedStatementSetter = ps -> {
            ps.setInt(1, tag.getId());
            ps.setInt(2, company.getId());
        };
        jdbcTemplate.update(INSERT_COMPANY_TAG_SQL, preparedStatementSetter);
    }


}
