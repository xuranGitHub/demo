package com.baturu.mybatis.diy.datasource.unpooled;

import com.baturu.mybatis.diy.datasource.pooled.PooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuran on 2016/12/26.
 */
public class PooledDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        PooledDataSource pooledDataSource = new PooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/main-test", "root", "123456");
        List<Connection> connections = new ArrayList<Connection>();

        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("start");
                    Connection connection = pooledDataSource.getConnection();
                    connections.add(connection);
                    Statement statement = connection.createStatement();
                    //boolean flag = statement.execute("CREATE TABLE main_id (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY ( id ))");
                    int count = statement.executeUpdate("INSERT INTO main_id VALUES ()");
                    ResultSet resultSet = statement.executeQuery("select count(*) from main_id");
                    while (resultSet.next()) {
                        int a = resultSet.getInt("count(*)");
                        System.out.println("--------" + a);
                    }
                    Thread.sleep(4000);
                    connection.close();
                    System.out.println(connection);
                    System.out.println("...");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            });
            thread.start();
            System.out.println("run");
        }
        System.out.println("done");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
