package com.xpizza.classical;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源池
 */
public class Pool {

    /** 资源池其实就是包了一个数据库连接list的容器 */
    private List<Connection> connections = new ArrayList<>(5);


    public static void main(String[] args){
        Pool pool = new Pool();
        System.out.print(pool.connections.size());
    }

}
