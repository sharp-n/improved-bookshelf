package com.company.tomcat_server;

import com.company.User;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.URLConstants;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(
        name = "UserLoginServlet",
        urlPatterns = {"/" + URLConstants.LOGIN_PAGE}
)
public class UserLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"login.html"));
        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String login = req.getParameter(ParametersConstants.NAME);

        if (User.checkUserNameForValidity(login)){
            resp.sendRedirect( new URIBuilder().setPath(URLConstants.FILE_WORK_PAGE).addParameter(ParametersConstants.NAME,login).toString());
        }
        else resp.sendRedirect( new URIBuilder().setPath(URLConstants.LOGIN_PAGE).toString());
    }
}
