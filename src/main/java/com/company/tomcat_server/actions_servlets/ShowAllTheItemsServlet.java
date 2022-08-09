package com.company.tomcat_server.actions_servlets;

import com.company.ComparatorsForSorting;
import com.company.User;
import com.company.enums.FilesMenu;
import com.company.handlers.Librarian;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.DefaultItemHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.items.Item;
import com.company.table.HtmlTableBuilder;
import com.company.tomcat_server.constants.FileNameConstants;
import com.company.tomcat_server.constants.ParametersConstants;
import com.company.tomcat_server.servlet_service.ParametersFromURL;
import com.company.tomcat_server.servlet_service.ServletService;
import com.company.tomcat_server.constants.TemplatesConstants;
import com.company.tomcat_server.constants.URLConstants;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static com.company.tomcat_server.constants.ParametersConstants.NAME;
import static com.company.tomcat_server.constants.URLConstants.SLASH;

@WebServlet(
        name = "ShowAllTheItemsServlet",
        urlPatterns = {SLASH + URLConstants.SHOW_ALL_THE_ITEMS}
)
public class ShowAllTheItemsServlet extends HttpServlet {

    final ParametersFromURL param = new ParametersFromURL();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            ServletService servletService = new ServletService();
            param.getParametersFromURL(req);

            if (param.typeOfFileWork.equals(ParametersConstants.FILE_PER_TYPE)) {

                resp.sendRedirect(new URIBuilder().setPathSegments(URLConstants.FILE_WORK_PAGE).addParameter(NAME, NAME).toString());
            } else {
                ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out));
                projectHandler.fileSwitch(FilesMenu.getByOption(param.typeOfFileWork), new User(param.name));

                Librarian librarian = projectHandler.getLibrarian();

                ItemHandler<Item> itemHandler = new DefaultItemHandler(); // todo optimize handlers
                List<Item> items = librarian.workWithFiles.readToItemsList();
                items = itemHandler.getSortedItemsByComparator(items, ComparatorsForSorting.COMPARATOR_ITEM_BY_ID);
                List<List<String>> itemsAsStr = itemHandler.anyItemsToString(items);

                String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(), FileNameConstants.SHOW_ALL_THE_ITEMS_FILE));
                htmlCode = htmlCode.replace(TemplatesConstants.TABLE_TEMPLATE, new HtmlTableBuilder(itemHandler.columnTitles, itemsAsStr).generateTable());
                htmlCode = servletService.replaceTemplateByURL(htmlCode,TemplatesConstants.URL_ITEMS_MENU_TEMPLATE,URLConstants.CHOOSE_ITEM_PAGE,param);

                servletService.printHtmlCode(resp, htmlCode);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            new ServletService().printErrorPage(resp);
        }
    }

}
