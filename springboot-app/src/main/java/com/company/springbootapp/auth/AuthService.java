package com.company.springbootapp.auth;

public interface AuthService {
    boolean validateBasicAuthentication(String appUserName, String typeOfWork);
}
