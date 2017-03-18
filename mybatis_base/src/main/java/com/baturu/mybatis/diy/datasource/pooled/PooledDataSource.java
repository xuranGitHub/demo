package com.baturu.mybatis.diy.datasource.pooled;

import com.baturu.mybatis.diy.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * todo
 * 1.抽出idleConnections,activeConnections成一个新对象，优化加锁，提高并发
 * 2.当activeConnections存在超时占用时，销毁，及防止销毁后对引用的调用（proxy中检验conn是否还有效）
 * 3.数据统计
 * Created by xuran on 2016/12/21.
 */
public class PooledDataSource implements DataSource {


    private UnpooledDataSource dataSource;

    private final List<PooledConnection> idleConnections = new ArrayList<PooledConnection>();
    private final List<PooledConnection> activeConnections = new ArrayList<PooledConnection>();
    private final int poolMaximumActiveConnections = 10;
    private final int poolMaximumIdleConnections = 5;
    private final int poolTimeToWait = 2000;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        dataSource = new UnpooledDataSource();
    }

    public PooledDataSource(String driver, String url, String username, String password) {
        dataSource = new UnpooledDataSource(driver, url, username, password);
        expectedConnectionTypeCode = assembleConnectionTypeCode(url, username, password);
        //expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
    }

    public Connection getConnection() throws SQLException {
        return popConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return getConnection();
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    private synchronized Connection popConnection() throws SQLException {
        PooledConnection conn = null;
        while (conn == null) {
            if (idleConnections.size() > 0) {
                conn = idleConnections.remove(0);
                System.out.println("get from idle "+ conn.getConnection());
            } else if (activeConnections.size() < poolMaximumActiveConnections) {
                Connection connection = dataSource.getConnection();
                conn = new PooledConnection(connection, this);
                System.out.println("get from active " + conn.getConnection());
            } else {
                try {
                    System.out.println("wait once");
                    this.wait(poolTimeToWait);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        if (conn != null) {
            activeConnections.add(conn);
            conn.setExpectedConnectionTypeCode(expectedConnectionTypeCode);
        }

        return conn.getProxyConnection();
    }

    protected synchronized void pushConnection(PooledConnection pooledConnection) throws SQLException {
        if (!pooledConnection.getConnection().getAutoCommit()) {
            pooledConnection.getConnection().rollback();
        }
        activeConnections.remove(pooledConnection);
        if (idleConnections.size() < poolMaximumIdleConnections && pooledConnection.getExpectedConnectionTypeCode() == expectedConnectionTypeCode) {
            idleConnections.add(pooledConnection);
        } else {
            pooledConnection.getConnection().close();
        }
    }

    /*
       * Closes all active and idle connections in the pool
       */
    public synchronized void forceCloseAll() {
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
        for (int i = activeConnections.size(); i > 0; i--) {
            closeConnection(activeConnections.remove(i - 1));
        }
        for (int i = idleConnections.size(); i > 0; i--) {
            closeConnection(idleConnections.remove(i - 1));
        }
    }

    private void closeConnection(PooledConnection pooledConnection) {
        try {
            Connection realConn = pooledConnection.getConnection();
            if (!realConn.getAutoCommit()) {
                realConn.rollback();
            }
            realConn.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }

}