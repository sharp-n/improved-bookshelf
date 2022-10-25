package com.company.actions_servlets;

import com.company.*;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.ParametersForWeb;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

@WebServlet(
        name = "AddItemServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.ADD_PAGE}
)
public class AddItemServlet extends HttpServlet {

    private static final Logger log
            = Logger.getLogger(Main.class);

    final ParametersForWeb param = new ParametersForWeb();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            param.getParametersFromURL(req);
            ServletService servletService = new ServletService();
            WebAppService webAppService = new WebAppService();
            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_HTML_FILE));

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
            String formContent = new HTMLFormBuilder().genForm(projectHandler.getItemHandler().genAddFormContent(), URLConstants.ADD_PAGE);

            String table = webAppService.getTable(SortingMenu.ITEM_ID.getDbColumn(), projectHandler, param);
            htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, table);

            servletService.printHtmlCode(resp, htmlCode);
        } catch(Exception exception){
            log.error(exception.getMessage() + " : " + HttpServlet.class.getSimpleName() + " : doGet()");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ServletService servletService = new ServletService();
            ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out)); // todo optimize handlers
            projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
            FilesMenu option = FilesMenu.getByParameter(param.typeOfWork);
            projectHandler.fileSwitch(option, new User(param.name));
            ItemHandler itemHandler = projectHandler.getItemHandler();

            List<String> params = itemHandler.convertItemParametersMapToList(req.getParameterMap());

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.INFORM_PAGE_HTML_FILE));

            if (projectHandler.getLibrarian().addItem(itemHandler, params)) {
                htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.SUCCESS_MESSAGE_TEMPLATE + "added");
            } else {
                htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.FAIL_MESSAGE);
            }

            htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode, param);
            servletService.printHtmlCode(resp, htmlCode);
        } catch (Exception exception){
            log.error(exception.getMessage() + " : " + HttpServlet.class.getSimpleName() + " : doPost()");
        }
    }

}
