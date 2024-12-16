package com.cnbsoftware.reciperandomizermobileapp.dtos;

public class UserDto {

    public UserDto(String username, String password) {
        Username = username;
        Password = password;
        Role = 1;
    }

    public UserDto(String username, String password, String selectedLanguage) {
        Username = username;
        Password = password;
        SelectedLanguage = selectedLanguage;
        Role = 1;
    }

    public String Username;
    public String Password;
    public String SelectedLanguage;
    public int Role;
}
