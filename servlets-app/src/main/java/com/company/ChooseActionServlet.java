package com.company;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@WebServlet(
        name = "ChooseActionServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.CHOOSE_ACTION}
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
