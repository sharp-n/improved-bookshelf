package com.company.springbootapp.auth;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean validateBasicAuthentication(String appUserName, String typeOfWork) {
        return !appUserName.isEmpty()&& !typeOfWork.isEmpty();
    }

}