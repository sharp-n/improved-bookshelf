package com.company.tomcat_server;

import com.company.tomcat_server.servlet_service.ServletService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

@WebServlet(
        name = "AddBookServlet",
        urlPatterns = {"/add-book"}
)
public class AddBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("AddBookServlet.doGet");
        ServletOutputStream out = resp.getOutputStream();

        ServletService servletService = new ServletService();
        String htmlCode = servletService.getTextFromFile(Paths.get(servletService.pathToHTMLFilesDir.toString(),"add-book.html"));

        out.write(htmlCode.getBytes());
        out.flush();

        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("AddBookServlet.doPost");
        ServletOutputStream out = resp.getOutputStream();
        //File file = new File("src/main/webapp/WEB-INF/main.html");

        String htmlCode = "";
        //Scanner in = new Scanner (file);
        //while(in.hasNext()){
        //    htmlCode +=in.nextLine() + " " + "\n";
        //}

        //out.write(htmlCode.getBytes());
        //out.flush();
        //in.close();
        out.close();
        resp.setContentType("text/html");
        String title = req.getParameter("title");
        System.out.println(title);
        PrintWriter printWriter = resp.getWriter();
        printWriter.println("Title: " + title);
        printWriter.close();
    }


}
