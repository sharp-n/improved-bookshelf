package com.company.tomcat_server;

import com.company.handlers.ProjectHandler;
import com.company.tomcat_server.servlet_service.ParametersConstants;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.servlet_service.URLConstants;
import org.apache.tomcat.util.net.URL;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

@WebServlet(
        name = "ChooseItemServlet",
        urlPatterns = {URLConstants.CHOOSE_ITEM_PAGE}
)
public class ChooseItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"choose-item.html"));
        ServletOutputStream out = resp.getOutputStream();
        out.print(htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        ServletOutputStream out = resp.getOutputStream();
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in),new PrintWriter(out));

    }

}
