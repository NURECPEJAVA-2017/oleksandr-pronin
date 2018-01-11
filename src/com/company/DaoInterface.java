package com.company;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;


public interface DaoInterface {
    void createUser(Connection conn, User user) throws SQLException;

    void deleteUser(Connection conn, User user) throws SQLException;

    void updateUser(Connection conn, User user) throws SQLException;

    User find(Connection conn, Long user_id) throws SQLException;

    Vector<User> findAll(Connection conn) throws SQLException;
}
