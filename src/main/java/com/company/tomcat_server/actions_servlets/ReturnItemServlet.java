package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.Validator;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.*;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import static com.company.enums.SortingMenu.ITEM_ID;
import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ReturnItemServlet",
        urlPatterns = {SLASH + URLConstants.RETURN_PAGE}
)
public class ReturnItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();
    final ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        param.getParametersFromURL(req);

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_HTML_FILE));
        ProjectHandler projectHandler = servletService.genProjectHandlerFromParameters(param);
        String formContent = new HTMLFormBuilder().genForm(projectHandler.getItemHandler().genFormForGettingID(URLConstants.RETURN_PAGE), URLConstants.RETURN_PAGE);
        String table = servletService.getTable(SortingMenu.ITEM_ID.getDbColumn(), servletService, projectHandler, param);
        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
        htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, table);
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
                ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out)); // todo optimize handlers
                projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
                projectHandler.fileSwitch(FilesMenu.getByDBColumnName(param.typeOfFileWork), new User(param.name));
                boolean borrowed = projectHandler.getLibrarian().borrowItem(itemID, false);
                if (borrowed) {
                    message = MessageConstants.SUCCESS_MESSAGE_TEMPLATE + "returned";
                }
            }
            servletService.generateAndPrintHTMLCode(resp, message, param, FileNameConstants.INFORM_PAGE_HTML_FILE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
