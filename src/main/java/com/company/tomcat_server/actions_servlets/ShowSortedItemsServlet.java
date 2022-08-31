package com.company.tomcat_server.actions_servlets;

import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.*;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static com.company.tomcat_server.constants.ParametersConstants.COMPARATOR_PARAM;
import static com.company.tomcat_server.constants.ParametersConstants.NAME;
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

            ServletService servletService = new ServletService();
            ServletOutputStream out = resp.getOutputStream();
            if (param.typeOfFileWork.equals(ParametersConstants.FILE_PER_TYPE)) {
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(NAME, NAME).toString());
            }

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_HTML_FILE));
            resp.setContentType("text/html");

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);

            String table = servletService.getTable(comparator, servletService, projectHandler, param);
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE,table);
            htmlCode =  servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);

            out.write(htmlCode.getBytes());
            //out.println("<br><br><div align=\"center\">" + table + "</div>");
            out.flush();
            out.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }




}
