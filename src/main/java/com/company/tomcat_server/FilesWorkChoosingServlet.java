package com.company.tomcat_server;

import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
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
        name = "FilesWorkChoosingServlet",
        urlPatterns = {SLASH + URLConstants.FILE_WORK_PAGE}
)
public class FilesWorkChoosingServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

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
            if (!typeOfFileWork.equals(ParametersConstants.ONE_FILE)
                    &&!typeOfFileWork.equals(ParametersConstants.FILE_PER_TYPE)
                    &&!typeOfFileWork.equals(ParametersConstants.DATABASE)) {
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).toString());
            }
            resp.sendRedirect(new ServletService().buildURLWithParameters(URLConstants.CHOOSE_ITEM_PAGE,param.name,typeOfFileWork,""));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
