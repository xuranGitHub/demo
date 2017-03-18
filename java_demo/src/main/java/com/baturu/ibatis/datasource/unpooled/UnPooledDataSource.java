package com.baturu.ibatis.datasource.unpooled;

import org.apache.ibatis.io.Resources;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by xuran on 16/7/26.
 */
public class UnPooledDataSource implements DataSource {

    private ClassLoader driverClassLoader;
    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<String, Driver>();

    private String driver;
    private String url;
    private String username;
    private String password;

    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            registeredDrivers.put(d.getClass().getName(), d);
        }
    }

    public UnPooledDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public UnPooledDataSource() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        initializeDriver();
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    private synchronized void initializeDriver() throws SQLException {

        if (!registeredDrivers.containsKey(driver)) {
            Class<?> driverType;
            try {
                if (driverClassLoader != null) {
                    driverType = Class.forName(driver, true, driverClassLoader);
                } else {
                    driverType = Resources.classForName(driver);
                }
                // DriverManager requires the driver to be loaded via the system ClassLoader.
                // http://www.kfu.com/~nsayer/Java/dyn-jdbc.html
                Driver driverInstance = (Driver)driverType.newInstance();
                DriverManager.registerDriver(new DriverProxy(driverInstance));
                registeredDrivers.put(driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
            }
        }
    }
    private static class DriverProxy implements Driver {
        private Driver driver;

        DriverProxy(Driver d) {
            this.driver = d;
        }

        @Override
        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        @Override
        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        @Override
        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        @Override
        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        // @Override only valid jdk7+
        public Logger getParentLogger() {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }

    @Override
    public void setLoginTimeout(int loginTimeout) throws SQLException {
        DriverManager.setLoginTimeout(loginTimeout);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) throws SQLException {
        DriverManager.setLogWriter(logWriter);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public static void main(String[] args) throws SQLException {
        try {
            test();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.0.253:3306/main";
        String username = "ops";
        String password = "123";

//        driver="org.apache.derby.jdbc.EmbeddedDriver";
//        url="jdbc:derby:ibderby;create=true";
//        username="";
//        password="";
        UnPooledDataSource unPooledDataSource = new UnPooledDataSource(driver, url, username, password);
        Connection connection = unPooledDataSource.getConnection();
        assert connection != null;
    }

    public static void test() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.0.253:3306/main", "ops", "123");
        String sql = "update qpu_user set createTime = now() where orgId = 1";
        PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
        int count = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        System.out.println(count);
    }









































    public static void testMysql() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.0.253:3306/main";
        String username = "ops";
        String password = "123";

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from qp_order");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getDouble("");
    }

}
