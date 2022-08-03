package com.company.tomcat_server;

import com.company.User;
import com.company.handlers.ProjectHandler;
import com.company.tomcat_server.servlet_service.ParametersConstants;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.servlet_service.URLConstants;
import org.apache.http.client.utils.URIBuilder;

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
        name = "FilesWorkChoosingServlet",
        urlPatterns = {URLConstants.FILE_WORK_PAGE}
)
public class FilesWorkChoosingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"file-work-choose.html"));
        ServletOutputStream out = resp.getOutputStream();
        out.print(htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String workWithOneFile = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);

        ServletOutputStream out = resp.getOutputStream();
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in),new PrintWriter(out));

        User user = new User(req.getParameter(ParametersConstants.NAME));
        String typeOfWorkParam = "";
        if (workWithOneFile.equals(ParametersConstants.ONE_FILE)){
            projectHandler.initFileWork(projectHandler.genOneFileWorker(user));
            typeOfWorkParam = ParametersConstants.ONE_FILE;
        } else if (workWithOneFile.equals(ParametersConstants.FILE_PER_TYPE)){
            projectHandler.initFileWork(projectHandler.genFilePerTypeWorker(user));
            typeOfWorkParam = ParametersConstants.FILE_PER_TYPE;
        } else {
            resp.sendRedirect( new URIBuilder().setPath(URLConstants.FILE_WORK_PAGE).toString());
        }

        resp.sendRedirect( new URIBuilder()
                .setPath(URLConstants.CHOOSE_ITEM_PAGE)
                .addParameter(ParametersConstants.NAME,user.userName)
                .addParameter(ParametersConstants.FILE_PER_TYPE,typeOfWorkParam)
                .toString());

    }

}
