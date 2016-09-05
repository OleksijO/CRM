package com.becomejavasenior.jdbc.implJdbcTemplate;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.TypeOfPhone;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.ContactDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ContactDAOJdbcImplTest extends SpringDaoJdbcImplTests {
    @Autowired
    private ContactDAO contactDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    @Qualifier("companyDao")
    private CompanyDAO companyDAO;
    private User userForContactTest;
    private static final String DEFAULT_NAME = "Default Name";
    private static final Date DEFAULT_DATE = new Timestamp(new Date().getTime());
    private int contactTestId;


    @PostConstruct
    public void init(){
        userForContactTest = userDAO.getById(1);
    }

    @Before
    public void setUp() {
        contactTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (contactTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM contact WHERE id = " + Integer.toString(contactTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Contact clean up failed!", e);
            }
            contactTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Contact contactTest = new Contact();
        Assert.assertTrue("Contact ID before creation must be '0'", contactTest.getId() == 0);

        try {
            contactTestId = contactDAO.insert(contactTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty contact ID must be '0'", contactTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty contact ID must be '0'", contactTestId == 0);
        }

        contactTest.setName(DEFAULT_NAME);
        contactTest.setTypeOfPhone(TypeOfPhone.MOBILE);
        contactTest.setDateCreate(DEFAULT_DATE);
        contactTest.setCreator(userForContactTest);
        contactTest.setResponsibleUser(userForContactTest);
        contactTestId = contactDAO.insert(contactTest);

        Assert.assertNotNull("Contact creation failed", contactTest);
        Assert.assertTrue("Contact ID after creation must be not '0'", contactTestId > 0);
        Assert.assertNotNull("Contact date of creation must be not null", contactTest.getDateCreate());
        Assert.assertNotNull("Contact creator must be not null", contactTest.getCreator());
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Contact read by PK failed", contactDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";
        String updatedPhone = "Updated Phone";
        String updatedPosition = "Updated Position";
        String updatedSkype = "updated-skype";
        String updatedEmail = "updated@email.org";
        Timestamp updatedCreateDate = new Timestamp(1L << 41);
        User userForTestUpdate = userDAO.getById(2);
        Company companyForTestUpdate = companyDAO.getById(2);

        Contact contactTest = new Contact();
        contactTest.setName(DEFAULT_NAME);
        contactTest.setTypeOfPhone(TypeOfPhone.MOBILE);
        contactTest.setDateCreate(DEFAULT_DATE);
        contactTest.setCreator(userForContactTest);
        contactTest.setResponsibleUser(userForContactTest);
        contactTestId = contactDAO.insert(contactTest);
        Assert.assertNotNull("Contact before update must not be null", contactTest);

        contactTest.setName(updatedName);
        contactTest.setPosition(updatedPosition);
        contactTest.setTypeOfPhone(TypeOfPhone.OTHER);
        contactTest.setPhone(updatedPhone);
        contactTest.setSkype(updatedSkype);
        contactTest.setEmail(updatedEmail);
        contactTest.setDateCreate(updatedCreateDate);
        contactTest.setCreator(userForTestUpdate);
        contactTest.setResponsibleUser(userForTestUpdate);
        contactTest.setCompany(companyForTestUpdate);

        contactDAO.update(contactTest);

        Contact updatedContact = contactDAO.getById(contactTestId);
        Assert.assertNotNull("Contact after update is null", updatedContact);
        Assert.assertEquals("Contact name update failed", updatedName, updatedContact.getName());
        Assert.assertEquals("Contact position update failed", updatedPosition, updatedContact.getPosition());
        Assert.assertEquals("Contact type of phone update failed", TypeOfPhone.OTHER.ordinal(), updatedContact.getTypeOfPhone().ordinal());
        Assert.assertEquals("Contact phone update failed", updatedPhone, updatedContact.getPhone());
        Assert.assertEquals("Contact skype update failed", updatedSkype, updatedContact.getSkype());
        Assert.assertEquals("Contact email update failed", updatedEmail, updatedContact.getEmail());
        Assert.assertEquals("Date of contact creation update failed", updatedCreateDate, updatedContact.getDateCreate());
        Assert.assertEquals("Contact creator update failed", userForTestUpdate.getId(), updatedContact.getCreator().getId());
        Assert.assertEquals("Contact responsible user update failed", userForTestUpdate.getId(), updatedContact.getResponsibleUser().getId());
        Assert.assertEquals("Contact link to Company update failed", companyForTestUpdate.getId(), updatedContact.getCompany().getId());
    }

    @Test
    public void testDelete() {
        Contact contactTest = new Contact();
        contactTest.setName(DEFAULT_NAME);
        contactTest.setTypeOfPhone(TypeOfPhone.MOBILE);
        contactTest.setDateCreate(DEFAULT_DATE);
        contactTest.setCreator(userForContactTest);
        contactTest.setResponsibleUser(userForContactTest);
        contactTestId = contactDAO.insert(contactTest);

        List contactList = contactDAO.getAll();
        int oldListSize = contactList.size();
        Assert.assertTrue("Contact list must not be empty", oldListSize > 0);

        contactDAO.delete(contactTestId);
        contactList = contactDAO.getAll();
        Assert.assertEquals("Contact delete test failed", 1, oldListSize - contactList.size());
        Assert.assertNull("Contact delete test failed", contactDAO.getById(contactTestId));
    }

    @Test
    public void testGetAll() {
        List contactList = contactDAO.getAll();
        Assert.assertNotNull("Contact list must not be null", contactList);
        Assert.assertTrue("Contact list must not be empty", contactList.size() > 0);
    }
}