package com.company.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean validateBasicAuthentication(String appUserName, String typeOfWork) {
        System.out.println("authService: " + appUserName + " - " + typeOfWork);
        return appUserName != null && typeOfWork != null && !appUserName.isEmpty() && !typeOfWork.isEmpty();
    }

}