package com.company.actions_servlets;

import com.company.*;
import com.company.servlet_service.HTMLFormBuilder;
import com.company.servlet_service.ParametersFromURL;
import com.company.servlet_service.ServletService;
import com.company.FileNameConstants;
import com.company.MessageConstants;
import com.company.TemplatesConstants;
import com.company.URLConstants;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    final ParametersFromURL param = new ParametersFromURL();
    final ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
            param.getParametersFromURL(req);

            String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_HTML_FILE));

            ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
            String formContent = new HTMLFormBuilder().genForm(projectHandler.getItemHandler().genFormForGettingID(URLConstants.DELETE_PAGE),URLConstants.DELETE_PAGE);
            String table = servletService.getTable(SortingMenu.ITEM_ID.getDbColumn(), servletService, projectHandler, param);
            htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, table);
            htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
            htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode, param);

            servletService.printHtmlCode(resp, htmlCode);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Integer itemID = servletService.parseParamToInt(req.getParameter(ITEM_ID.getDbColumn()));
            itemID = Validator.staticValidateID(itemID);

            String message = MessageConstants.FAIL_MESSAGE;

            if (itemID != null) {
                ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
                projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
                projectHandler.fileSwitch(FilesMenu.getByDBColumnName(param.typeOfFileWork), new User(param.name));
                boolean deleted = projectHandler.getLibrarian().deleteItem(itemID, true);
                if (deleted) {
                    message = MessageConstants.SUCCESS_MESSAGE_TEMPLATE + "deleted";
                }
            }
            servletService.generateAndPrintHTMLCode(resp, message, param, FileNameConstants.INFORM_PAGE_HTML_FILE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }


}
