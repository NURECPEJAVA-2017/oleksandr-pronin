package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class HsqldbUserDao implements DaoInterface
{
    public void createUser(Connection conn, User user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("INSERT INTO user (firstName, lastName, dateOfBirth) VALUES (?, ?, ?)");
        statement.setString(1, user.firstName());
        statement.setString(2, user.lastName());
        statement.setDate(3, user.dateOfBirth());

        statement.executeUpdate();
        statement.close();
    }

    public void deleteUser(Connection conn, User user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM user WHERE id = ?");
        statement.setLong(1, user.id());

        statement.executeUpdate();
        statement.close();
    }

    public void updateUser(Connection conn, User user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE user SET firstName = ?, lastName = ?, dateOfBirth = ? WHERE id = ?");
        statement.setString(1, user.firstName());
        statement.setString(2, user.lastName());
        statement.setDate(3, user.dateOfBirth());
        statement.setLong(4, user.id());

        statement.executeUpdate();
        statement.close();
    }

    public User find(Connection conn, Long user_id) throws SQLException {
        User user = null;

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
        statement.setLong(1, user_id);

        ResultSet set = statement.executeQuery();

        while (set.next()) {
            Long id = set.getLong("id");
            String firstName = set.getString("firstName");
            String lastName = set.getString("lastName");
            java.sql.Date dateOfBirth = set.getDate("dateOfBirth");

            user = new User(id, firstName, lastName, dateOfBirth);
        }

        statement.close();
        return user;
    }

    public Vector<User> findAll(Connection conn) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM user");
        ResultSet set = statement.executeQuery();

        Vector<User> users = new Vector<User>();

        while (set.next()) {
            Long id = set.getLong("id");
            String firstName = set.getString("firstName");
            String lastName = set.getString("lastName");
            java.sql.Date dateOfBirth = set.getDate("dateOfBirth");

            User user = new User(id, firstName, lastName, dateOfBirth);
            users.add(user);
        }

        statement.close();
        return users;
    }

    public void printUser(User user) {
        System.out.print(user.id());
        System.out.println(". " + user.firstName() + " " + user.lastName() + "\t" + user.dateOfBirth());
    }

    public void printUsers(Vector<User> users) {
        for (int i = 0; i < users.size(); ++i) {
            User user = users.elementAt(i);
            printUser(user);
        }
    }

    public void handle(Connection conn) throws ParseException, SQLException {
        DateFormat format = new SimpleDateFormat("d.M.y");
        java.util.Date parsed = format.parse("01.01.1970");
        java.sql.Date date = new java.sql.Date(parsed.getTime());

        User user = new User("John", "Travolta", date);
        User user2 = new User("Donald", "Trump", date);

        createUser(conn, user);
        createUser(conn, user2);

        Vector<User> users = findAll(conn);
        System.out.println("loaded " + users.size() + " users:");
        printUsers(users);

        Long user_id = new Long(0);
        User found_user = find(conn, user_id);

        if (found_user != null) {
            System.out.println("user with id " + user_id + ":");
            printUser(found_user);

            System.out.println("changing name of user:");
            found_user.setFirstName("Frank");
            updateUser(conn, found_user);
            printUser(found_user);

            System.out.println("users after update:");
            users = findAll(conn);
            printUsers(users);

            System.out.println("deleting user:");
            deleteUser(conn, found_user);
        }

        System.out.println("current users:");
        users = findAll(conn);
        printUsers(users);
    }
}
