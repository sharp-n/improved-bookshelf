package com.company.springbootapp.interceptors;

import com.company.springappconstants.CookieNames;
import com.company.springbootapp.auth.AuthService;
import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@AllArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    CookieUtil cookieUtil;

    @Autowired
    private AuthService authService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userName = cookieUtil.getCookies(request).get(CookieNames.USER_NAME);
        String typeOfWork = cookieUtil.getCookies(request).get(CookieNames.TYPE_OF_FILE_WORK);

//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.sendRedirect("/login?url=" + request.getRequestURI());
        return authService.validateBasicAuthentication(userName, typeOfWork);

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}


