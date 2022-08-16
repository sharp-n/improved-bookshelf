package com.company.tomcat_server.actions_servlets;

import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.URLConstants;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.TemplatesConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static com.company.tomcat_server.constants.FormConstants.COMPARATOR_PARAM;
import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ShowSortedItemsServlet",
        urlPatterns = {SLASH + URLConstants.SHOW_ITEMS}
)
public class ShowSortedItemsServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();
    private String htmlCode = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        param.getParametersFromURL(req);

        ServletService servletService = new ServletService();
        htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_HTML_FILE));
        HTMLFormBuilder htmlFormBuilder = new HTMLFormBuilder();
        String form = htmlFormBuilder.genForm(
                Objects.requireNonNull(ItemHandlerProvider.getHandlerByClass(
                                ItemHandlerProvider.getClassBySimpleNameOfClass(param.typeOfItem))).
                        genSortFormContent(),
                "show");
        htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, form);

        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode, param);
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String comparator = req.getParameter(COMPARATOR_PARAM);

            ServletOutputStream out = resp.getOutputStream();

            String table = new ServletService().genTableOfSortedItems(comparator,param);
            resp.setContentType("text/html");
            out.write(htmlCode.getBytes());
            out.println("<br><br><div align=\"center\">" + table + "</div>");
            out.flush();
            out.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }


}
