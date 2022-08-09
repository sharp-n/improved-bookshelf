package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.Validator;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.*;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "BorrowItemServlet",
        urlPatterns = {SLASH + URLConstants.BORROW_PAGE}
)
public class BorrowItemServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletOutputStream out = resp.getOutputStream(); // todo optimize usage

        param.getParametersFromURL(req);

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),FileNameConstants.ACTIONS_REALIZATION_FILE));

        String formContent = ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(param.typeOfItem)).genFormForGettingID(URLConstants.BORROW_PAGE);

        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,param);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Integer itemID = servletService.parseParamToInt(req.getParameter(FormConstants.ITEM_ID_PARAM));
        itemID = Validator.staticValidateID(itemID);

        String message = "O-ops! Something goes wrong...";
        if (itemID!=null){
            ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
            projectHandler.itemMenuSwitch(MainMenu.getByOption(param.typeOfItem));
            projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfFileWork), new User(param.name));
            boolean borrowed = projectHandler.getLibrarian().borrowItem(itemID,true,projectHandler.getItemHandler());
            if (borrowed) {
                message = "Item is successfully borrowed";
            }
        }
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.INFORM_PAGE_FILE)); // todo optimize usage
        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,param).replace(TemplatesConstants.MESSAGE_TEMPLATE,message);
        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();
        out.close();
    }

}
