package com.company.auth;

public interface AuthService {
    boolean validateBasicAuthentication(String appUserName, String typeOfWork);
}
