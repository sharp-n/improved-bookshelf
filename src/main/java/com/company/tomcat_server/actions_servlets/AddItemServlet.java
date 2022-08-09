package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.*;
import com.company.tomcat_server.servlet_service.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "AddItemServlet",
        urlPatterns = {SLASH + URLConstants.ADD_PAGE}
)
public class AddItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        param.getParametersFromURL(req);
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.ACTIONS_REALIZATION_FILE));

        String formContent = new HTMLFormBuilder().genForm(ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(param.typeOfItem)).genAddFormContent(),URLConstants.ADD_PAGE);

        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);

        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletService servletService = new ServletService();


        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfFileWork), new User(param.name));
        ItemHandler itemHandler = projectHandler.getItemHandler();

        List<String> params = itemHandler.convertItemParametersMapToList(req.getParameterMap());

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),FileNameConstants.INFORM_PAGE_FILE));

        if (projectHandler.getLibrarian().addItem(itemHandler,params)){
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.SUCCESS_MESSAGE);
        } else {
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.FAIL_MESSAGE);
        }

        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,param);
        servletService.printHtmlCode(resp, htmlCode);
    }

}
