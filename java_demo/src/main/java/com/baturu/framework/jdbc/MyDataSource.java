package com.baturu.framework.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Created by xuran on 16/5/24.
 */
public class MyDataSource {

    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driverClass = null;

    private static LinkedList<Connection> pool = new LinkedList<Connection>() ;

    /**
     * 注册数据库驱动
     */
    static {
        try {
            String resource = "db.properties";
            ClassLoader[] classLoaders = new ClassLoader[]{MyDataSource.class.getClassLoader(), Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemClassLoader()};
            InputStream in = null;
            for (ClassLoader classLoader : classLoaders) {
                in = classLoader.getResourceAsStream(resource);
                if (null != in) {
                    break;
                }
            }
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            driverClass = prop.getProperty("driverClass");
            Class.forName(driverClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化连接池
     */
    private MyDataSource() {

        for (int i = 0; i < 10; i ++) {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                pool.add(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从连接池获取链接
     */
    public Connection getConnection() {
        return pool.remove();
    }

    /**
     *  回收连接对象
     * @param conn
     */
    public void release(Connection conn) {
        System.out.println("");
        pool.addLast(conn);
    }

    public int getLength() {
        return pool.size();
    }

    public static void main(String[] args) {
        MyDataSource mds = new MyDataSource() ;
        Connection conn = null ;
        for (int i = 0 ; i < 20 ; i ++) {
            conn = mds.getConnection() ;
            System.out.println(conn+"被获取；连接池还有："+mds.getLength());
            mds.release(conn) ;
        }
    }
}
