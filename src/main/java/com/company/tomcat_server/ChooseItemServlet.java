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

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ChooseItemServlet",
        urlPatterns = {SLASH + URLConstants.CHOOSE_ITEM_PAGE}
)
public class ChooseItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.CHOOSE_ITEM_FILE));

        param.getParametersFromURL(req);

        htmlCode = htmlCode
                .replace(TemplatesConstants.FORM_TEMPLATE,new DefaultItemHandler().genItemChoosingForm()) // todo optimize
                .replace(TemplatesConstants.URL_SHOW_TEMPLATE, new URIBuilder()
                        .setPathSegments(URLConstants.SHOW_ALL_THE_ITEMS)
                        .addParameter(ParametersConstants.NAME,param.name)
                        .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE,param.typeOfFileWork)
                        .toString())
                .replace(TemplatesConstants.ERL_TYPE_OF_FILE_WORK_TEMPLATE,new URIBuilder()
                        .setPathSegments(URLConstants.FILE_WORK_PAGE)
                        .addParameter(ParametersConstants.NAME,param.name)
                        .toString());
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
            resp.sendRedirect(new URIBuilder()
                    .setPathSegments(URLConstants.CHOOSE_ACTION, typeOfItem.toLowerCase()) // todo optimize
                    .addParameter(ParametersConstants.NAME, param.name)
                    .addParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE, param.typeOfFileWork)
                    .addParameter(ParametersConstants.TYPE_OF_ITEM, typeOfItem)
                    .toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
