package com.xpizza.classical;

import com.xpizza.bass.lang.Numbers;
import com.xpizza.bass.util.PropertyUtil;

import java.sql.*;
import java.util.Map;

/**
 * Database Util
 */
public class DBUtil {

    public static Pool pool = null;

    static {
        Map<String, String> properties = PropertyUtil.getProperties("jdbc.properties");
        try {
            int poolSize = Numbers.toInt(properties.get("pool.size"));
            pool = new Pool(
                    properties.get("pool.name"),
                    poolSize,
                    properties.get("jdbc.driver"),
                    properties.get("jdbc.url"),
                    properties.get("jdbc.username"),
                    properties.get("jdbc.password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection() {
        return pool.getFreeConnection();
    }

    /**
     * 释放数据库资源工具
     * @param rs
     * @param stmt
     * @param conn
     * @throws SQLException
     */
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

    /**
     * 释放数据库资源工具
     * @param stmt
     * @param conn
     * @throws SQLException
     */
    public static void release(Statement stmt, Connection conn)  {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            conn.giveBack();
        }
    }

}
