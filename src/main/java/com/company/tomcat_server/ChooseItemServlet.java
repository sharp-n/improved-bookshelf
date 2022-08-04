package com.company.tomcat_server;

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
import java.nio.file.Paths;

@WebServlet(
        name = "ChooseItemServlet",
        urlPatterns = {URLConstants.CHOOSE_ITEM_PAGE}
)
public class ChooseItemServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"choose-item.html"));

        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);

        htmlCode = htmlCode.replace("{{URL}}",
                new URIBuilder().setPath(URLConstants.SHOW_ALL_THE_ITEMS)
                        .addParameter(ParametersConstants.NAME,name)
                        .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfFileWork)
                        .toString());

        ServletOutputStream out = resp.getOutputStream();
        out.print(htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
        resp.sendRedirect( new URIBuilder()
                .setPathSegments(URLConstants.CHOOSE_ITEM_PAGE,typeOfItem)
                .addParameter(ParametersConstants.NAME,name)
                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfFileWork)
                .addParameter(ParametersConstants.TYPE_OF_ITEM,typeOfItem)
                .toString());
    }

}
