package com.company.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean validateBasicAuthentication(String appUserName, String typeOfWork) {
        return !appUserName.isEmpty()&& !typeOfWork.isEmpty();
    }

}