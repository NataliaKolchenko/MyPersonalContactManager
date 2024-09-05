package com.example.MyPersonalContactManager.repository;

import com.example.MyPersonalContactManager.models.UserModels.User;
import com.example.MyPersonalContactManager.repository.interfaces.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepositoryImp implements UserRepositoryInterface<User> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId((rs.getString("User_Id")));
        user.setUserName(rs.getString("User_Name"));
        user.setLogin(rs.getString("Login"));
        user.setPassword(rs.getString("Password"));
        user.setRole(rs.getBoolean("User_Role"));
        user.setCreateDate(rs.getTimestamp("Create_Date").toLocalDateTime());
        user.setLastUpdateDate(rs.getTimestamp("Last_Update_Date").toLocalDateTime());
        return user;
    };

    @Override
    public User createUser(User user) {
        String insertSql = "INSERT INTO Users (User_Id, User_Name, Login, Password, User_Role, Create_Date, Last_Update_Date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSql,
                user.getUserId().toString(),
                user.getUserName(),
                user.getLogin(),
                user.getPassword(),
                user.getRole(),
                user.getCreateDate().toLocalDate(),
                user.getLastUpdateDate().toLocalDate());
        return user;
    }

    @Override
    public User getUserById(String userId) {
        String selectSql = "SELECT * FROM Users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(selectSql, userRowMapper, userId);
    }

    @Override
    public User getUserByLogin(String login) {
        String selectSql = "SELECT * FROM Users WHERE login = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, userRowMapper, login);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        String selectSql = "SELECT * FROM Users";
        return jdbcTemplate.query(selectSql, userRowMapper);
    }

    @Override
    public boolean deleteUserById(String userId) {
        String deleteSql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(deleteSql, userId) > 0;
    }

    @Override
    public User updateUser(User user) {
        String updateSql = "UPDATE users SET User_Role = ?, Login = ?, Password = ?, User_Name = ?, " +
                "Last_Update_Date = ? WHERE User_Id = ?";
        jdbcTemplate.update(updateSql,
                user.getRole(),
                user.getLogin(),
                user.getPassword(),
                user.getUserName(),
                user.getLastUpdateDate(),
                user.getUserId().toString());
        return user;
    }

    @Override
    public String getToken(String userId) {
        String selectSql = "SELECT Token FROM Users_Token WHERE User_Id = ?";
        try {
            return jdbcTemplate.queryForObject(selectSql, new Object[]{userId}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "";
        }
    }

    @Override
    public void saveToken(String token, String userId) {
        if (getToken(userId).isEmpty()) {
            String insertTokenSql = "INSERT INTO Users_Token (Token, User_Id, Create_Date, Last_Update_Date) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertTokenSql, token, userId, LocalDateTime.now(), LocalDateTime.now());
        } else {
            String updateTokenSql = "UPDATE Users_Token SET Token = ?, Last_Update_Date = ? WHERE User_Id = ?";
            jdbcTemplate.update(updateTokenSql, token, LocalDateTime.now(), userId);
        }
    }

    public boolean getUserRoleByToken(String token) {
        String userId = getUserIdByToken(token);
        if (userId == null || userId.isEmpty()) {
            return false;
        }
        String selectRole = "SELECT User_Role FROM Users WHERE User_Id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(selectRole, new Object[]{userId}, Boolean.class));
    }

    @Override
    public String getUserIdByToken(String token) {
        String selectUserId = "SELECT User_Id FROM Users_Token WHERE Token = ?";
        try {
            return jdbcTemplate.queryForObject(selectUserId, new Object[]{token}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return "";
        }
    }
}

