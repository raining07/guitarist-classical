package com.xpizza.classical;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Pool pool = new Pool("sqllite",5,"jdbc:sqlite:test.db", null,null, "org.sqlite.JDBC");
        Connection connection = pool.getFreeConnection();
        java.sql.Connection sqlConn = connection.getConnection();
        Statement stmt = sqlConn.createStatement();
        stmt.execute("CREATE TABLE if not EXISTS base01_class (" +
               "id VARCHAR(32) PRIMARY key," +
               "name VARCHAR(128) not NULL," +
               "am_or_pm VARCHAR(1)," + // am:1/pm:2
               "class_no VARCHAR(4))"); // 班级号
        stmt.close();
        System.out.println(pool.getFreeConnectionsCount());
        System.out.println(pool.getBusyConnectionsCount());
        System.out.println(pool.getSize());
    }

}
