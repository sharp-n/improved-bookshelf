package com.company.tomcat_server;

import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.URLConstants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ChooseActionServlet",
        urlPatterns = {SLASH + URLConstants.CHOOSE_ACTION}
)
public class ChooseActionServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_HTML_FILE));
        param.getParametersFromURL(req);
        Map<String,String> pathsWithParameters = servletService.addParamsToParametersMapValues(servletService.initURLTemplatesMap(),param);
        htmlCode = servletService.replaceURLInTemplate(htmlCode, pathsWithParameters);
        htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            String strURI = new ServletService().buildURLWithParameters(URLConstants.CHOOSE_ITEM_PAGE,param.name,param.typeOfFileWork,param.typeOfItem);
            resp.sendRedirect(strURI);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
