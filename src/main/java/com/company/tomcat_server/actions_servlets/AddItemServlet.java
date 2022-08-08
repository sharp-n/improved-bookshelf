package com.company.tomcat_server.actions_servlets;

import com.company.User;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.MessageConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.servlet_service.*;

import javax.servlet.ServletOutputStream;
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
        urlPatterns = {"/add"}
)
public class AddItemServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("AddBookServlet.doGet");
        ServletOutputStream out = resp.getOutputStream();

        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        System.out.println(typeOfItem);
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"actions-realization-template.html"));

        String formContent = new HTMLFormBuilder().genForm(ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(typeOfItem)).genAddFormContent(),"add");

        htmlCode = htmlCode.replace("{{FORM-CONTENT}}", formContent);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletService servletService = new ServletService();


        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(typeOfFileWork), new User(name));
        ItemHandler itemHandler = projectHandler.getItemHandler();

        List<String> params = itemHandler.convertItemParametersMapToList(req.getParameterMap());
        params.forEach(o->System.out.println(o));

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"inform-page-template.html"));

        if (projectHandler.getLibrarian().addItem(itemHandler,params)){
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.SUCCESS_MESSAGE);
        } else {
            htmlCode = htmlCode.replace(TemplatesConstants.MESSAGE_TEMPLATE, MessageConstants.FAIL_MESSAGE);
        }

        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,name,typeOfFileWork,typeOfItem);
        ServletOutputStream out = resp.getOutputStream();
        out.write(htmlCode.getBytes());
        out.flush();

    }



}
