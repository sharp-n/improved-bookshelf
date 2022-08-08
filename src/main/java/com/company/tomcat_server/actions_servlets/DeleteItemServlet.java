package com.company.tomcat_server.actions_servlets;

import com.company.Validator;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.constants.FormConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.constants.URLConstants;
import com.company.tomcat_server.servlet_service.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "DeleteItemServlet",
        urlPatterns = {SLASH + URLConstants.DELETE_PAGE}
)
public class DeleteItemServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";
    ServletService servletService = new ServletService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ServletOutputStream out = resp.getOutputStream();

        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"actions-realization-template.html"));

        String formContent = ItemHandlerProvider.getHandlerByClass(ItemHandlerProvider.getClassBySimpleNameOfClass(typeOfItem)).genFormForGettingID(URLConstants.DELETE_PAGE);
        htmlCode = htmlCode.replace(TemplatesConstants.FORM_TEMPLATE, formContent);
        htmlCode = servletService.replaceURLTemplatesInActionsPage(htmlCode,name,typeOfFileWork,typeOfItem);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {// todo implement this method

        Integer itemID = servletService.parseParamToInt(req.getParameter(FormConstants.ITEM_ID_PARAM));
        itemID = Validator.staticValidateID(itemID);


    }



}
