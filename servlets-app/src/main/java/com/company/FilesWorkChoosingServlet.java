package com.company;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(
        name = "FilesWorkChoosingServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.FILE_WORK_PAGE}
)
public class FilesWorkChoosingServlet extends HttpServlet {

    private static final org.apache.log4j.Logger log
            = Logger.getLogger(Main.class);

    final ParametersForWeb param = new ParametersForWeb();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html");
            ServletService servletService = new ServletService();
            param.getParametersFromURL(req);
            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.FILE_WORK_CHOOSE_HTML_FILE));
            servletService.printHtmlCode(resp, htmlCode);
        } catch (Exception exception){
            log.error(exception.getMessage() + " : " + FilesWorkChoosingServlet.class.getSimpleName() + " : doGet()");
        }
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
            log.error(ioException.getMessage() + " : " + FilesWorkChoosingServlet.class.getSimpleName() + " : doPost()");
            new ServletService().printErrorPage(resp);
        }
    }

}
