package com.theironyard.charlotte;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Ben on 8/30/16.
 */
public class MainTest {
    Connection conn;

    @Before
    public void initialize() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:test");
    }

    public Connection startConnection() throws SQLException {
        Main.createTables(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        User user = Main.selectUser(conn, "Alice");
        conn.close();

        assertTrue(user != null);
    }

    @Test
    public void testMessage() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        User user = Main.selectUser(conn, "Alice");

        Main.insertMessage(conn, user.getId(), -1, "Hello, world!");
        Message message = Main.selectMessage(conn, 1);
        conn.close();

        assertTrue(message != null);
    }

    @Test
    public void testReplies() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        Main.insertUser(conn, "Bob", "");

        User alice = Main.selectUser(conn, "Alice");
        User bob = Main.selectUser(conn, "Bob");

        Main.insertMessage(conn, alice.getId(), -1, "Hello, world!");
        Main.insertMessage(conn, bob.getId(), 1, "This is a reply!");
        Main.insertMessage(conn, bob.getId(), 1, "This is another reply!");

        ArrayList<Message> replies = Main.selectReplies(conn, 1);
        conn.close();

        assertTrue(replies.size() == 2);
    }
}