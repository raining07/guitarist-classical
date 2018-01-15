package com.xpizza.classical;

import java.sql.*;

public class Test {

    public static Connection getConn() {
        // String url = "jdbc:mysql://192.168.0.100:3306/shinning";
        String url = "jdbc:mysql://172.16.11.137:3306/shinning";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.jdbc.Driver";
        Connection conn = null;
        try {
            Class.forName(driver); // classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 释放数据库资源工具 BEGIN
    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        release(stmt, conn);
    }

    public static void release(Statement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
