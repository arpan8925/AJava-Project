package com.scs.dao;

import com.scs.model.User;
import com.scs.util.HibernateUtil;
import com.scs.util.PasswordUtil;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Integration test for UserDAO.
 * Requires MySQL running and schema.sql executed.
 * Connection settings come from src/main/resources/hibernate.cfg.xml.
 */
public class UserDAOTest {

    @AfterClass
    public static void tearDown() {
        HibernateUtil.shutdown();
    }

    @Test
    public void insertAndFetchUser() {
        UserDAO dao = new UserDAO();

        String email = "junit-" + System.currentTimeMillis() + "@scs.com";
        User u = new User("JUnit User", email, PasswordUtil.hash("junit123"), User.Role.CITIZEN);
        u.setArea("TestArea");
        u.setPhone("9999999999");

        Long id = dao.save(u);
        assertNotNull("save should return generated id", id);
        assertTrue("id should be positive", id > 0);

        User fetched = dao.findById(id);
        assertNotNull("user should be retrievable by id", fetched);
        assertEquals(email, fetched.getEmail());
        assertEquals(User.Role.CITIZEN, fetched.getRole());
        assertTrue("password hash should verify", PasswordUtil.verify("junit123", fetched.getPassword()));

        User byEmail = dao.findByEmail(email);
        assertNotNull("findByEmail should return the saved user", byEmail);
        assertEquals(id, byEmail.getId());

        dao.delete(id);
        assertNull("user should be deleted", dao.findById(id));
    }

    @Test
    public void adminUserExistsFromSeed() {
        UserDAO dao = new UserDAO();
        User admin = dao.findByEmail("admin@scs.com");
        assertNotNull("seed admin should exist", admin);
        assertEquals(User.Role.ADMIN, admin.getRole());
        assertTrue("seed admin password should be admin123",
                PasswordUtil.verify("admin123", admin.getPassword()));
    }
}
