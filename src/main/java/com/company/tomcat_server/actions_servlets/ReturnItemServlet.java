package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.Validator;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.FormConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.constants.URLConstants;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.TemplatesConstants;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

@WebServlet(
        name = "ReturnItemServlet",
        urlPatterns = {"/" + URLConstants.RETURN_PAGE}
)
public class ReturnItemServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";

    ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletOutputStream out = resp.getOutputStream(); // todo optimize usage

        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"actions-realization-template.html"));

        String formContent = ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(typeOfItem)).genFormForGettingID("return");

        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,name,typeOfFileWork,typeOfItem);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Integer itemID;
        try{
            itemID = Integer.parseInt(req.getParameter(FormConstants.ITEM_ID_PARAM));

        } catch (NumberFormatException nfe){
            itemID = -1;
        }
        itemID = Validator.staticValidateID(itemID);

        String message = "O-ops! Something goes wrong...";
        if (itemID!=null){
            ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
            projectHandler.itemMenuSwitch(MainMenu.getByOption(typeOfItem));
            projectHandler.fileSwitch(FilesMenu.getByOption(typeOfFileWork), new User(name));
            boolean borrowed = projectHandler.getLibrarian().borrowItem(itemID,false,projectHandler.getItemHandler());
            if (borrowed) {
                message = "Item is successfully borrowed";
            }
        }
        // todo optimize
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"inform-page-template.html"));
        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,name,typeOfFileWork,typeOfItem).replace(TemplatesConstants.MESSAGE_TEMPLATE,message);
        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();
        out.close();
    }

}
