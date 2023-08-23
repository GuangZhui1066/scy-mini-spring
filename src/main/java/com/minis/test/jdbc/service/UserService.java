package com.minis.test.jdbc.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.minis.batis.SqlSession;
import com.minis.batis.SqlSessionFactory;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.RowMapper;
import com.minis.test.jdbc.entity.User;

public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    public User getUserInfo(int id) {
        final String sql = "select id, name, age, birthday from user where id = " + id;

        return (User) jdbcTemplate.query(
            (stmt) -> {
                ResultSet rs = stmt.executeQuery(sql);
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(id);
                    user.setName(rs.getString("name"));
                    user.setAge(rs.getInt("age"));
                    user.setBirthday(new Date(rs.getDate("birthday").getTime()));
                }
                return user;
            }
        );
    }

    public User getUserInfo2(int id) {
        final String sql = "select id, name, age, birthday from user where id = ?";
        return (User) jdbcTemplate.query(sql, new Object[]{id},
            (pstmt) -> {
                ResultSet rs = pstmt.executeQuery();
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(id);
                    user.setName(rs.getString("name"));
                    user.setAge(rs.getInt("age"));
                    user.setBirthday(new Date(rs.getDate("birthday").getTime()));
                }
                return user;
            }
        );
    }

    public User getUserInfo3(String name, Date birthday) {
        final String sql = "select id, name, age, birthday from user where name = ? and birthday = ?";
        return (User) jdbcTemplate.query(sql, new Object[]{name, birthday},
            (pstmt) -> {
                ResultSet rs = pstmt.executeQuery();
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setAge(rs.getInt("age"));
                    user.setBirthday(new Date(rs.getDate("birthday").getTime()));
                }
                return user;
            }
        );
    }

    /**
     * 使用Lambda表达式不会产生额外的嵌套类的class文件，而使用匿名内部类会产生一个class文件 (UserService$1.class)
     */
    public List<User> getUserList(int idStart) {
        final String sql = "select id, name, age, birthday from user where id > ?";
        return (List<User>) jdbcTemplate.query(sql, new Object[] {idStart},
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setAge(rs.getInt("age"));
                    user.setBirthday(new Date(rs.getDate("birthday").getTime()));
                    return user;
                }
            }
        );
    }

    public User getUserById(int userId) {
        String sqlId = "com.minis.test.jdbc.entity.User.getUserById";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (User) sqlSession.selectOne(sqlId, new Object[]{userId},
            (pstmt) -> {
                ResultSet rs = pstmt.executeQuery();
                User user = null;
                if (rs.next()) {
                    user = new User();
                    user.setId(userId);
                    user.setName(rs.getString("name"));
                    user.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                }
                return user;
            }
        );
    }

    public int updateUser(User user) {
        String sqlId = "com.minis.test.jdbc.entity.User.updateById";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.update(sqlId,
            new Object[]{user.getName(), user.getAge(), user.getBirthday(), user.getId()});
    }

}
