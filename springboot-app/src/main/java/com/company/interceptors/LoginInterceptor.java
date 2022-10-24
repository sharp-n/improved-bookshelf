package com.company.interceptors;

import com.company.springappconstants.CookieNames;
import com.company.auth.AuthService;
import com.company.utils.CookieUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

        boolean auth = authService.validateBasicAuthentication(userName, typeOfWork);
        if (!auth){
            response.sendRedirect("/login");
        }

//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.sendRedirect("/login?url=" + request.getRequestURI());
        return auth;
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


