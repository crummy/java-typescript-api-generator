package com.malcolmcrum.typescriptapigenerator.demo.services;

import com.malcolmcrum.typescriptapigenerator.demo.api.UsersApi;
import com.malcolmcrum.typescriptapigenerator.demo.dtos.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersService implements UsersApi {
    private final AtomicInteger userIdCounter = new AtomicInteger(1);
    private final Map<Integer, UserDto> users = new HashMap<>();

    public UsersService() {
        addUser("admin");
        addUser("jimbo");
        addUser("james");
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
    public UserDto addUser(String username) {
        int id = userIdCounter.getAndIncrement();
        UserDto user = new UserDto(id, username);
        users.put(id, user);
        return user;
    }
}
