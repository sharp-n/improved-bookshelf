package com.company.tomcat_server;

import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.tomcat_server.servlet_service.ParametersConstants;
import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(
        name = "ReturnItemServlet",
        urlPatterns = {"/return"}
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

        htmlCode = htmlCode.replace("{{FORM-CONTENT}}", formContent);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }


}
