package com.cnbsoftware.reciperandomizermobileapp.dtos;

public class UserDto {

    public UserDto(String username, String password) {
        Username = username;
        Password = password;
        Role = 1;
    }

    public String Username;
    public String Password;
    public int Role;
}
