package com.baturu.mybatis.demo.util;

import com.baturu.mybatis.demo.dto.Role;
import com.baturu.mybatis.demo.mapper.RoleMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xuran on 2017/1/9.
 */
public class SqlSessionFactoryUtil {
    private static SqlSessionFactory sqlSessionFactory = null;
    private static final Class CLASS_LOCK = SqlSessionFactoryUtil.class;
    private SqlSessionFactoryUtil() {}
    private static SqlSessionFactory initSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            Logger.getLogger(SqlSessionFactoryUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        synchronized (CLASS_LOCK) {
            if (sqlSessionFactory == null) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            }
        }
        return sqlSessionFactory;
    }

    public static SqlSession openSqlSession() {
        if (sqlSessionFactory == null) {
            initSqlSessionFactory();
        }
        return sqlSessionFactory.openSession();
    }

    private static void init() {
        DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/mybatis", "root", "123456");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setLazyLoadingEnabled(true);
        configuration.setUseActualParamName(false); // to test legacy style reference (#{0} #{1})
        configuration.getTypeAliasRegistry().registerAlias(Role.class);
        configuration.addMapper(RoleMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
