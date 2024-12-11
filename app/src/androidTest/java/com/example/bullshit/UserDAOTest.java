package com.example.bullshit;

import android.content.Context;

import androidx.room.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserDAOTest {

    private AppDatabase db;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        userDAO = db.userDAO();
    }

    @After
    public void tearDown() throws IOException {
        db.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testUser1", "password123", false);
        userDAO.insertUser(user);

        List<User> users = userDAO.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("testUser1", users.get(0).getUsername());
    }

    @Test
    public void testGetNonAdminUsers() {
        User user1 = new User("testUser1", "password123", false);
        User user2 = new User("adminUser", "admin123", true);

        userDAO.insertUser(user1);
        userDAO.insertUser(user2);

        List<User> nonAdminUsers = userDAO.getNonAdminUsers();
        assertEquals(1, nonAdminUsers.size());
        assertEquals("testUser1", nonAdminUsers.get(0).getUsername());
    }
}
