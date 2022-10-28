package com.company.utils;


import com.company.controllers.LoginController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Repository
public class CookieUtil {

    private static final Logger log
            = Logger.getLogger(CookieUtil.class);

    public void createCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name,value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(31*24*60*60);
        response.addCookie(cookie);
        System.out.println("Created cookie - " + name);
    }

    public Map<String, String> getCookies(HttpServletRequest request){
        try {
            Cookie[] cookiesArr = request.getCookies();
            List<Cookie> cookieList = new ArrayList<>(Arrays.asList(cookiesArr));
            Map<String, String> cookies = new HashMap<>();
            cookieList.stream()
                    .filter(cookie -> !"JSESSION".equals(cookie.getName()))
                    .forEach(cookie -> cookies.put(cookie.getName(), cookie.getValue()));
            return cookies;
        } catch (NullPointerException nullPointerException){
            log.error(nullPointerException.getMessage() + ":" + CookieUtil.class.getSimpleName());
            return new HashMap<>();
        }
    }

}
