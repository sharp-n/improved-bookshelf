package com.company;

import com.company.servlet_service.ServletService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

import static com.company.URLConstants.SLASH;

@WebServlet(
        name = "UserLoginServlet",
        urlPatterns = {SLASH + URLConstants.LOGIN_PAGE}
)
public class UserLoginServlet extends HttpServlet {

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
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }
}
