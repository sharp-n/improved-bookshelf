package com.company.actions_servlets;

import com.company.*;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.company.enums.SortingMenu.ITEM_ID;

@WebServlet(
        name = "DeleteItemServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.DELETE_PAGE}
)
public class DeleteItemServlet extends HttpServlet {

    final ParametersForWeb param = new ParametersForWeb();
    final ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
            param.getParametersFromURL(req);

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_HTML_FILE));

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
            String formContent = new HTMLFormBuilder().genForm(projectHandler.getItemHandler().genFormForGettingID(URLConstants.DELETE_PAGE),URLConstants.DELETE_PAGE);
            String table = new WebAppService().getTable(SortingMenu.ITEM_ID.getDbColumn(), projectHandler, param);
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, table);
            htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
            htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode, param);

            servletService.printHtmlCode(resp, htmlCode);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Integer itemID = servletService.parseParamToInt(req.getParameter(ITEM_ID.getDbColumn()));
        itemID = Validator.staticValidateID(itemID);

        String message = MessageConstants.FAIL_MESSAGE;

        if (itemID != null) {
            ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
            projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
            projectHandler.fileSwitch(FilesMenu.getByParameter(param.typeOfWork), new User(param.name));
            boolean deleted = projectHandler.getLibrarian().deleteItem(itemID, true);
            if (deleted) {
                message = MessageConstants.SUCCESS_MESSAGE_TEMPLATE + "deleted";
            }
        }
        servletService.generateAndPrintHTMLCode(resp, message, param, FileNameConstants.INFORM_PAGE_HTML_FILE);
    }


}
