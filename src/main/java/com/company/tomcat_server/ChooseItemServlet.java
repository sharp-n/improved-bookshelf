package com.company.tomcat_server;

import com.company.handlers.item_handlers.DefaultItemHandler;
import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.constants.URLConstants;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Locale;

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ChooseItemServlet",
        urlPatterns = {SLASH + URLConstants.CHOOSE_ITEM_PAGE}
)
public class ChooseItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.CHOOSE_ITEM_FILE));

        param.getParametersFromURL(req);

        htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_SHOW_TEMPLATE,URLConstants.SHOW_ALL_THE_ITEMS,param)
                .replace(TemplatesConstants.FORM_TEMPLATE,new DefaultItemHandler().genItemChoosingForm());
        htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_TYPE_OF_FILE_WORK_TEMPLATE,URLConstants.FILE_WORK_PAGE,param);
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
            String strURI = new ServletService().buildURLWithParameters(URLConstants.CHOOSE_ACTION+"/"+typeOfItem.toLowerCase(),param.name,param.typeOfFileWork,typeOfItem);
            resp.sendRedirect(strURI);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
