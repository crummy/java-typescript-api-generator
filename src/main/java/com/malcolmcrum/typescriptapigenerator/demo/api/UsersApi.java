package com.malcolmcrum.typescriptapigenerator.demo.api;

import com.malcolmcrum.typescriptapigenerator.demo.dtos.UserDto;

import java.util.Collection;

public interface UsersApi {
    Collection<UserDto> getUsers();

    UserDto getUser(int userId);

    void addUser(UserDto user);
}
