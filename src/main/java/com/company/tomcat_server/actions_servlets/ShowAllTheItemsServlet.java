package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.sqlite.DBService;
import com.company.tomcat_server.constants.*;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import static com.company.tomcat_server.constants.ParametersConstants.NAME;
import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ShowAllTheItemsServlet",
        urlPatterns = {SLASH + URLConstants.SHOW_ALL_THE_ITEMS}
)
public class ShowAllTheItemsServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            ServletService servletService = new ServletService();
            param.getParametersFromURL(req);

            if (param.typeOfFileWork.equals(ParametersConstants.FILE_PER_TYPE)) {
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(NAME, NAME).toString());
            }

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_HTML_FILE));

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
            DBService dbService = new DBService();
            dbService.open();
            User user = new User(param.name);

            String table = "";
            if(param.typeOfFileWork.equals(ParametersConstants.DATABASE)) {
                table = servletService.genTableOfSortedItemsFromDB(dbService,projectHandler,user);
            }
            else if (param.typeOfFileWork.equals(ParametersConstants.ONE_FILE))  {
                table = servletService.genTableOfSortedItemsFromFiles(param, SortingMenu.ITEM_ID.getDbColumn());
            }
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, table);
            htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);
            servletService.printHtmlCode(resp, htmlCode);

        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
