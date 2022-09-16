package com.company;

import com.company.handlers.item_handlers.DefaultItemHandler;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

@WebServlet(
        name = "ChooseItemServlet",
        urlPatterns = {URLConstants.SLASH + URLConstants.CHOOSE_ITEM_PAGE}
)
public class ChooseItemServlet extends HttpServlet {

    final ParametersForWeb param = new ParametersForWeb();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.CHOOSE_ITEM_HTML_FILE));

        param.getParametersFromURL(req);

        htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_SHOW_TEMPLATE,URLConstants.SHOW_ALL_THE_ITEMS,param)
                .replace(TemplatesConstants.FORM_TEMPLATE,new DefaultItemHandler(new PrintWriter(System.out),new Scanner(System.in)).genItemChoosingForm());
        htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_TYPE_OF_FILE_WORK_TEMPLATE,URLConstants.FILE_WORK_PAGE,param);
        servletService.printHtmlCode(resp, htmlCode);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
            String strURI = new ServletService().buildURLWithParameters(URLConstants.CHOOSE_ACTION,param.name,param.typeOfFileWork,typeOfItem);
            resp.sendRedirect(strURI);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
