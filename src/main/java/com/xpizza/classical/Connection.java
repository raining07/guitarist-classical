package com.xpizza.classical;

import com.xpizza.bass.lang.StringUtil;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 高程度封装的数据库连接
 */
public class Connection {

    private java.sql.Connection connection;

    private boolean busy = false;

    public java.sql.Connection getConnection() {
        return connection;
    }

    public void setConnection(java.sql.Connection connection) {
        this.connection = connection;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    /**
     * 构造器
     * @param driver
     * @param url
     * @param username
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver); // classLoader,加载对应驱动
        if(StringUtil.isEmpty(username)){
            connection = DriverManager.getConnection(url);
        }else{
            connection = DriverManager.getConnection(url, username, password);
        }
    }

    /**
     * 归还连接
     */
    public void giveBack(){
        setBusy(false);
    }

    /**
     * 释放资源
     * @throws SQLException
     */
    public void release() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
        connection = null;
        giveBack();
    }
}
