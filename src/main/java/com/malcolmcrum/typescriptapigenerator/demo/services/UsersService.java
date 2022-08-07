package com.malcolmcrum.typescriptapigenerator.demo.services;

import com.malcolmcrum.typescriptapigenerator.demo.api.UsersApi;
import com.malcolmcrum.typescriptapigenerator.demo.dtos.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UsersService implements UsersApi {
    private final Map<Integer, UserDto> users = new HashMap<>();

    public UsersService() {
        var admin = new UserDto(1, "admin");
        var user = new UserDto(2, "jimbo");
        addUser(admin);
        addUser(user);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return users.values();
    }

    @Override
    public UserDto getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public void addUser(UserDto user) {
        users.put(user.userId(), user);
    }
}
