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
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "ChooseActionServlet",
        urlPatterns = {URLConstants.CHOOSE_ACTION}
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

        Map<String, String> paths = new HashMap<>();
        paths.put("{{URL-ADD}}","add");
        paths.put("{{URL-DELETE}}","delete");
        paths.put("{{URL-TAKE}}","take");
        paths.put("{{URL-RETURN}}","return");
        paths.put("{{URL-SHOW}}","show");
        Map<String,String> pathsWithParameters = addParamsToParametersMapValues(paths);
        htmlCode = replaceURLInTemplate(htmlCode, pathsWithParameters);

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

    private String replaceURLInTemplate(String htmlCode, Map<String, String> paths){
        for (Map.Entry<String, String> path : paths.entrySet()) {
            htmlCode = htmlCode.replace(path.getKey(),path.getValue());
        }
        return htmlCode;
    }

    private Map<String, String> addParamsToParametersMapValues(Map<String,String> paths){
        for(Map.Entry<String,String> path : paths.entrySet()){
            path.setValue(new ServletService().addParams(name,typeOfFileWork,typeOfItem).setPathSegments(path.getValue()).toString());
        }
        return paths;
    }

}
