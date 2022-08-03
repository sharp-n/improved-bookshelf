package com.company.tomcat_server;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@WebServlet(
        name = "MainMenuServlet",
        urlPatterns = {"/main"}
)
public class MainMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {



        resp.setContentType("text/html");

        System.out.println("MainServlet.doGet");

        //HttpSession session = req.getSession();
        ////new String(Files.readAllBytes(Paths.get("src/main/resources/html.template"))).replace("{{PAGE_TITLE}}", "").replace("{{}}", "");
//
        //Integer visitCounter = (Integer) session.getAttribute("visitCounter");
        //if (visitCounter == null) {
        //    visitCounter = 1;
        //} else {
        //    visitCounter++;
        //}
        //session.setAttribute("visitCounter", visitCounter);
        //String username = req.getParameter("username");
//
        //resp.setContentType("text/html");
//
        //PrintWriter printWriter = resp.getWriter();
//
        //printWriter.write("<a href=\"html\\form.html\">form</a>");
//
        //printWriter.close();
        ////printWriter.println();
        ////if (username == null) {
        ////    printWriter.write("Hello, Anonymous" + "<br>");
        ////} else {
        ////    printWriter.write("Hello, " + username + "<br>");
        ////}
        ////printWriter.write("Page was visited " + visitCounter + " times.");
        ////printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String login = request.getParameter("login");
        System.out.println(login);

        ServletOutputStream out = response.getOutputStream();
        File file = new File("src/main/webapp/main.html");
//
        String htmlCode = "";
        Scanner in = new Scanner(file);
        while(in.hasNext()){
            htmlCode +=in.nextLine() + " " + "\n";
        }
//
        out.write(htmlCode.getBytes());
        out.flush();
        in.close();

        response.setContentType("text/html");
        System.out.println("MainServlet.doPost");
    }

}