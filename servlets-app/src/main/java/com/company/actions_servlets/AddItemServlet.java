package com.company.actions_servlets;

import com.company.*;
import com.company.servlet_service.*;
import com.company.FileNameConstants;
import com.company.MessageConstants;
import com.company.TemplatesConstants;
import com.company.URLConstants;
import com.company.servlet_service.HTMLFormBuilder;
import com.company.servlet_service.ParametersFromURL;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

@WebServlet(
        name = "AddItemServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.ADD_PAGE}
)
public class AddItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        param.getParametersFromURL(req);
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_HTML_FILE));

        ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
        String formContent = new HTMLFormBuilder().genForm(projectHandler.getItemHandler().genAddFormContent(),URLConstants.ADD_PAGE);

        String table = servletService.getTable(SortingMenu.ITEM_ID.getDbColumn(), servletService, projectHandler, param);
        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
        htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE,table);

        servletService.printHtmlCode(resp, htmlCode);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletService servletService = new ServletService();
        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out)); // todo optimize handlers
        projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
        FilesMenu option = FilesMenu.getByDBColumnName(param.typeOfFileWork);
        projectHandler.fileSwitch(option, new User(param.name));
        ItemHandler itemHandler = projectHandler.getItemHandler();

        List<String> params = itemHandler.convertItemParametersMapToList(req.getParameterMap());

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),FileNameConstants.INFORM_PAGE_HTML_FILE));

        params.forEach(o->System.out.println(o));

        if (projectHandler.getLibrarian().addItem(itemHandler,params)){
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.SUCCESS_MESSAGE_TEMPLATE + "added");
        } else {
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.FAIL_MESSAGE);
        }

        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,param);
        servletService.printHtmlCode(resp, htmlCode);
    }

}
