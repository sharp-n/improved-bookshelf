package com.company.actions_servlets;

import com.company.*;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;

import jakarta.servlet.ServletOutputStream;
import org.apache.http.client.utils.URIBuilder;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@WebServlet(
        name = "ShowSortedItemsServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.SHOW_ITEMS}
)
public class ShowSortedItemsServlet extends HttpServlet {

    private static final Logger log
            = Logger.getLogger(Main.class);

    final ParametersForWeb param = new ParametersForWeb();
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
            String comparator = req.getParameter(ParametersConstants.COMPARATOR_PARAM);

            ServletService servletService = new ServletService();
            ServletOutputStream out = resp.getOutputStream();
            if (param.typeOfWork.equals(ParametersConstants.FILE_PER_TYPE)) {
                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(ParametersConstants.NAME, ParametersConstants.NAME).toString());
            }

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_HTML_FILE));
            resp.setContentType("text/html");

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);

            String table = new WebAppService().getTable(comparator, projectHandler, param);
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE,table);
            htmlCode =  servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);

            out.write(htmlCode.getBytes());
            //out.println("<br><br><div align=\"center\">" + table + "</div>");
            out.flush();
            out.close();
        } catch (IOException exception) {
            log.error(exception.getMessage() + " : " + ShowSortedItemsServlet.class.getSimpleName() + " : doPost()");
            new ServletService().printErrorPage(resp);
        }
    }




}
