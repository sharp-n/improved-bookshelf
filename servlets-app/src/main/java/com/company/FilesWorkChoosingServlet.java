package com.company;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(
        name = "FilesWorkChoosingServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.FILE_WORK_PAGE}
)
public class FilesWorkChoosingServlet extends HttpServlet {

    Logger LOGGER;

    final ParametersForWeb param = new ParametersForWeb();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html");
        ServletService servletService = new ServletService();
        param.getParametersFromURL(req);
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.FILE_WORK_CHOOSE_HTML_FILE));
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
            String url = "";
            if (!typeOfFileWork.equals(ParametersConstants.ONE_FILE)
                    &&!typeOfFileWork.equals(ParametersConstants.FILE_PER_TYPE)
                    &&!typeOfFileWork.equals(ParametersConstants.DATABASE_SQLite)
                    &&!typeOfFileWork.equals(ParametersConstants.DATABASE_MYSQL)) {
                url = new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).toString();
            } else {
                url = new ServletService().buildURLWithParameters(URLConstants.CHOOSE_ITEM_PAGE,param.name,typeOfFileWork,"");
            }
            if(typeOfFileWork.equals(ParametersConstants.DATABASE_SQLite)
                    ||typeOfFileWork.equals(ParametersConstants.DATABASE_MYSQL)){
                DBService dbService = new SQLiteDBService();
                dbService.open(DBServiceProvider.getDBNameByService(dbService));
                dbService.createTablesIfNotExist(dbService.getConnection());
                dbService.createUser(new User(param.name),dbService.getConnection());
            }
            resp.sendRedirect(url);
        } catch (IOException ioException) {
            LOGGER.info(ioException.getMessage());
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
