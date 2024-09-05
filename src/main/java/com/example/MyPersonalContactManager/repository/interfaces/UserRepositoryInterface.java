package com.example.MyPersonalContactManager.repository.interfaces;

import java.util.List;

public interface UserRepositoryInterface<T> {

    T createUser(T user);

    T getUserById(String userId);

    T getUserByLogin(String login);

    List<T> getAllUsers();

    boolean deleteUserById(String userId);

    T updateUser(T user);

    String getToken(String userId);

    void saveToken(String token, String userId);

    String getUserIdByToken(String token);

    boolean getUserRoleByToken(String token);
}

