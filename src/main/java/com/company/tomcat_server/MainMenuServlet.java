package com.company.tomcat_server;

import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

@WebServlet(
        name = "MainMenuServlet",
        urlPatterns = {"/main"}
)
public class MainMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        ServletOutputStream out = resp.getOutputStream();

        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"main.html"));
        out.write(htmlCode.getBytes());
        out.flush();

        System.out.println("MainServlet.doGet");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        System.out.println(login);

        response.setContentType("text/html");
        System.out.println("MainServlet.doPost");
    }

}