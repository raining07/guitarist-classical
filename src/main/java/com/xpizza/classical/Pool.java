package com.xpizza.classical;

import com.xpizza.bass.lang.exception.BassExcetion;
import com.xpizza.bass.util.Assert;

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

    /** Connection配置 */
    private String url;

    private String username;

    private String password;

    private String driver;

    /** 资源池其实就是包了一个数据库连接list的容器 */
    private List<Connection> connections;

    /**
     * 资源池构造器
     * @param name 资源池名称
     * @param size 资源池大小
     * @param url jdbc url
     * @param username jdbc 用户名
     * @param password jdbc 密码
     * @param driver jdbc 驱动名称
     */
    public Pool(String name, int size, String url, String username, String password, String driver) {
        this.name = name;
        this.size = size;
        this.url = url;
        this.username = username;
        this.password = password;
        this.driver = driver;
        init();
    }

    /**
     * 初始化资源池
     */
    public void init(){
        if(connections == null){
            connections = new ArrayList<>(size);
        }else{
            connections.clear();
        }
        for(int i=0; i<size; i++){

        }
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

}
