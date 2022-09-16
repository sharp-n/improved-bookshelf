package com.company.actions_servlets;

import com.company.*;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.ServletService;
import com.company.FileNameConstants;
import com.company.ParametersConstants;
import com.company.TemplatesConstants;
import com.company.URLConstants;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

@WebServlet(
        name = "ShowAllTheItemsServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.SHOW_ALL_THE_ITEMS}
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
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(ParametersConstants.NAME, ParametersConstants.NAME).toString());
            }

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_HTML_FILE));

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);

            User user = new User(param.name);

            String table = "";
            if (param.typeOfFileWork.equals(ParametersConstants.DATABASE_SQLite)
                    ||param.typeOfFileWork.equals(ParametersConstants.DATABASE_MYSQL)) {
                DBService dbService = DBServiceProvider.getDBServiceByOption(param.typeOfFileWork);
                dbService.open(DBServiceProvider.getDBNameByService(dbService));
                dbService.createTablesIfNotExist(dbService.getConnection());
                dbService.createUser(user,dbService.getConnection());
                table = servletService.genTableOfSortedItemsFromDB(dbService,projectHandler,user);
            } else if (param.typeOfFileWork.equals(ParametersConstants.ONE_FILE))  {
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
