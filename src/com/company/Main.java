package com.company;

import java.sql.*;



class Main
{
    public static void createUserTable(Connection conn) {
        String query = "CREATE TABLE user (id INTEGER IDENTITY PRIMARY KEY, " +
                "firstName VARCHAR(32), lastName VARCHAR(32), dateOfBirth DATE)";

        try {
            CallableStatement statement = conn.prepareCall(query);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Connection makeConnection() {
        Connection conn = null;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:file:db", "SA", "");
        } catch (java.sql.SQLException e) {
            System.out.println(e);
        }

        return conn;
    }

    public static void main(String[] args) {
        System.out.println("application started");
        Connection conn = makeConnection();
        createUserTable(conn);

        HsqldbUserDao db = new HsqldbUserDao();

        if (conn != null) {
            try {
                db.handle(conn);
                conn.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}