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
        name = "UserLoginServlet",
        urlPatterns = {"/login"}
)
public class UserLoginServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("UserLoginServlet.doGet");

        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"login.html"));

        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("UserLoginServlet.doPost");
        String login = req.getParameter("login");
        System.out.println(login);
        System.out.println("UserLoginServlet.doGet");

        if (login.equals("yana")){
            File file = new File("src/main/webapp/main.html");
            ServletOutputStream out = resp.getOutputStream();
            String htmlCode = "";
            Scanner in = new Scanner (file);
            while(in.hasNext()){
                htmlCode +=in.nextLine() + " " + "\n";
            }
            out.write(htmlCode.getBytes());
            out.flush();
            in.close();
        }

        else doGet(req,resp);
    }
}
