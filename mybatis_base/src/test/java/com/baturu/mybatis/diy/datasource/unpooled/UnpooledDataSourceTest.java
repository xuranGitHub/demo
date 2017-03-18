package com.baturu.mybatis.diy.datasource.unpooled;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

/**
 * Created by xuran on 2016/12/21.
 */
public class UnpooledDataSourceTest {

    @Test
    public void testDriverCount() throws SQLException {
        int oldCount = getDriversCount();
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/main-test");
        unpooledDataSource.setUsername("root");
        unpooledDataSource.setPassword("123456");
        unpooledDataSource.getConnection();
        int nextCount = getDriversCount();
        unpooledDataSource = new UnpooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/main-test");
        unpooledDataSource.setUsername("root");
        unpooledDataSource.setPassword("123456");
        unpooledDataSource.getConnection();
        int newCount = getDriversCount();
        assertEquals(newCount, newCount);
    }

    @Test
    public void testSqlFromConn() throws SQLException {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/main-test");
        unpooledDataSource.setUsername("root");
        unpooledDataSource.setPassword("123456");
        Connection connection = unpooledDataSource.getConnection();
//        boolean auto_commit = connection.getAutoCommit();
//        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        //boolean flag = statement.execute("CREATE TABLE main_id (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY ( id ))");
        int count = statement.executeUpdate("INSERT INTO main_id VALUES ()");
        ResultSet resultSet = statement.executeQuery("select count(*) from main_id");
        while (resultSet.next()) {
            int a = resultSet.getInt("count(*)");
            System.out.println(a);
        }
        connection.close();
    }

    private int getDriversCount() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        int count = 0;
        while (drivers.hasMoreElements()) {
            drivers.nextElement();
            count ++;
        }
        return count;
    }

    @Test
    public void testStr() {
        String method = new String("");
        String close = new String("0.01");
        BigDecimal money = new BigDecimal("0.01");
        boolean equals = method.equals(close);
        boolean hash = method.hashCode() == close.hashCode();
        String[] strs = method.split("\\&");
        System.out.println(money.toString());
        assert equals & hash;
    }
}
