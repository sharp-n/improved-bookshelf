package com.company;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;

import static com.company.URLConstants.SLASH;

@WebServlet(
        name = "UserLoginServlet",
        urlPatterns = {SLASH + URLConstants.LOGIN_PAGE}
)
public class UserLoginServlet extends HttpServlet {

    private static final Logger log
            = Logger.getLogger(Main.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.LOGIN_HTML_FILE));
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String login = req.getParameter(ParametersConstants.NAME);
            if (User.checkUserNameForValidity(login)) {
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(ParametersConstants.NAME, login).toString());
            } else resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.LOGIN_PAGE).toString());

        } catch(IOException ioException){
            log.error(ioException.getMessage() + " : " + UserLoginServlet.class.getSimpleName() + " : doPost()");
            new ServletService().printErrorPage(resp);
        }
    }
}
