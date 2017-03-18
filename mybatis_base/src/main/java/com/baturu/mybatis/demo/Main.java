package com.baturu.mybatis.demo;

import com.baturu.mybatis.demo.dto.Role;
import com.baturu.mybatis.demo.mapper.RoleMapper;
import com.baturu.mybatis.demo.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by xuran on 2017/1/9.
 */
public class Main {

    public static void main(String[] args) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            Role role = roleMapper.getRole(1L);
            System.out.println(role.getRoleName());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            sqlSession.rollback();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
