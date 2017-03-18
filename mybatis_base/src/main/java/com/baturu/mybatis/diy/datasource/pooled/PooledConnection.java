package com.baturu.mybatis.diy.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * Created by xuran on 2016/12/21.
 */
public class PooledConnection implements InvocationHandler {

    private static final String CLOSE = "close";
    private static final Class<?>[] IFACES = new Class<?>[] { Connection.class };

    private Connection connection;

    public Connection getProxyConnection() {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    private PooledDataSource dataSource;

    private int expectedConnectionTypeCode;

    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.connection = connection;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            dataSource.pushConnection(this);
            return null;
        }
        return method.invoke(connection, args);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    public void setExpectedConnectionTypeCode(int expectedConnectionTypeCode) {
        this.expectedConnectionTypeCode = expectedConnectionTypeCode;
    }

}
