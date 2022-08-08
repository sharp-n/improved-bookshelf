package com.company.tomcat_server;

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
        name = "FilesWorkChoosingServlet",
        urlPatterns = {"/" + URLConstants.FILE_WORK_PAGE}
)
public class FilesWorkChoosingServlet extends HttpServlet {

    String name = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        name = req.getParameter(ParametersConstants.NAME);
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"file-work-choose.html"));
        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String workWithOneFile = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);

        String typeOfWorkParam = "";
        if (workWithOneFile.equals(ParametersConstants.ONE_FILE)){
            typeOfWorkParam = ParametersConstants.ONE_FILE;
        } else if (workWithOneFile.equals(ParametersConstants.FILE_PER_TYPE)){
            typeOfWorkParam = ParametersConstants.FILE_PER_TYPE;
        } else {
            resp.sendRedirect( new URIBuilder().setPath(URLConstants.FILE_WORK_PAGE).toString());
        }

        resp.sendRedirect( new URIBuilder()
                .setPath(URLConstants.CHOOSE_ITEM_PAGE)
                .addParameter(ParametersConstants.NAME, name)
                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfWorkParam)
                .toString());
    }

}
