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
import java.util.Map;

@WebServlet(
        name = "ChooseActionServlet",
        urlPatterns = {"/" + URLConstants.CHOOSE_ACTION}
)
public class ChooseActionServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"actions-template.html"));
        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        Map<String,String> pathsWithParameters = servletService.addParamsToParametersMapValues(servletService.initURLTemplatesMap(),name,typeOfFileWork,typeOfItem);
        htmlCode = servletService.replaceURLInTemplate(htmlCode, pathsWithParameters);

        ServletOutputStream out = resp.getOutputStream();
        out.print(htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect( new URIBuilder()
                .setPathSegments(URLConstants.CHOOSE_ITEM_PAGE,typeOfItem)
                .addParameter(ParametersConstants.NAME, name)
                .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,typeOfFileWork)
                .addParameter(ParametersConstants.TYPE_OF_ITEM,typeOfItem)
                .toString());
    }



}
