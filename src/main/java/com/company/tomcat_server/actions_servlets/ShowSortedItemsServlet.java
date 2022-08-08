package com.company.tomcat_server.actions_servlets;

import com.company.ComparatorsForSorting;
import com.company.User;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.DefaultItemHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.handlers.item_handlers.ItemHandlerProvider;
import com.company.items.Item;
import com.company.table.HtmlTableBuilder;
import com.company.tomcat_server.servlet_service.HTMLFormBuilder;
import com.company.tomcat_server.servlet_service.ParametersConstants;
import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static com.company.tomcat_server.servlet_service.FormConstants.COMPARATOR_PARAM;

@WebServlet(
        name = "ShowSortedItemsServlet",
        urlPatterns = {"/show"}
)
public class ShowSortedItemsServlet extends HttpServlet {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";
    String htmlCode = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();

        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);

        ServletService servletService = new ServletService();
        htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"actions-realization-template.html"));
        HTMLFormBuilder htmlFormBuilder = new HTMLFormBuilder();
        String form = htmlFormBuilder.genForm(
                ItemHandlerProvider.getHandlerByClass(
                        ItemHandlerProvider.getClassBySimpleNameOfClass(typeOfItem)).
                        genSortFormContent(),
                "show");

        htmlCode = htmlCode.replace("{{FORM-CONTENT}}", form);

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String comparator = req.getParameter(COMPARATOR_PARAM);


        ServletOutputStream out = resp.getOutputStream();

        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
        projectHandler.itemMenuSwitch(MainMenu.getByOption(typeOfItem));
        projectHandler.fileSwitch(FilesMenu.getByOption(typeOfFileWork), new User(name));
        projectHandler.fileSwitch(FilesMenu.getByOption(typeOfFileWork), new User(name));
        SortingMenu sortingParam = SortingMenu.getByOption(comparator);
        ItemHandler itemHandler = projectHandler.getItemHandler();
        Librarian librarian = projectHandler.getLibrarian();
        List<Item> items = librarian.initSortingItemsByComparator(librarian.workWithFiles, sortingParam,itemHandler);
        List<List<String>> itemsAsStr = itemHandler.anyItemsToString(items);

        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(itemHandler.columnTitles,itemsAsStr);

        String table = tableBuilder.generateTable();
        resp.setContentType("text/html");
        out.write(htmlCode.getBytes());
        out.println("<br><br><div align=\"center\">" + table + "</div>");
        out.flush();
        out.close();
    }
}
