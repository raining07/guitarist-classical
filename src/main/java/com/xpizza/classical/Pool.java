package com.xpizza.classical;

import com.xpizza.bass.lang.exception.BassExcetion;
import com.xpizza.bass.util.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源池
 */
public class Pool {

    /** 资源池名称 */
    private String name;

    /** 资源池中的资源数量 */
    private int size;

    /** 资源池其实就是包了一个数据库连接list的容器 */
    private List<Connection> connections;

    /* Connection配置BEGIN */
    private String url;
    private String username;
    private String password;
    private String driver;
    /* Connection配置END */

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }

    /**
     * 资源池构造器
     * @param name 资源池名称
     * @param size 资源池大小
     * @param url jdbc url
     * @param username jdbc 用户名
     * @param password jdbc 密码
     * @param driver jdbc 驱动名称
     */
    public Pool(String name, int size, String driver, String url, String username, String password) throws SQLException, ClassNotFoundException {
        this.name = name;
        this.size = size;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        init();
    }

    /**
     * 初始化资源池
     */
    public void init() throws SQLException, ClassNotFoundException {
        if(connections == null){
            connections = new ArrayList<>(size);
        }else{
            connections.clear();
        }
        for(int i=0; i<size; i++){
            Connection connection = new Connection(driver, url, username, password);
            connections.add(connection);
        }
    }

    /**
     * 摧毁资源池
     * @throws SQLException
     */
    public void destroy() throws SQLException {
        for(int i=0; i<size; i++){
            Connection connection = connections.get(i);
            connection.release();
        }
        connections = null;
    }

    /**
     * 得到空闲的连接
     */
    public Connection getFreeConnection() {
        Connection connection = null;
        for (int i = 0; i < size; i++) {
            connection = connections.get(i);
            if (connection.isBusy()) {
                continue;
            }
            connection.setBusy(true);
            return connection;
        }
        Assert.isNotNull(connection, "资源池中没有空闲连接");
        return null;
    }

    /**
     * 获取资源池内空闲连接的数量
     * @return
     */
    public int getFreeConnectionsCount(){
        int count = 0;
        Connection connection = null;
        for (int i = 0; i < size; i++) {
            connection = connections.get(i);
            if(!connection.isBusy()){
                count ++;
            }
        }
        return count;
    }

    /**
     * 获取资源池内被占用的连接的数量
     * @return
     */
    public int getBusyConnectionsCount(){
        return this.size - getFreeConnectionsCount();
    }

}
