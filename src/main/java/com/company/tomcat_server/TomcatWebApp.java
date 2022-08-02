package com.company.tomcat_server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.utils.URIBuilder;

public class TomcatWebApp {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
//        tomcat.setHostname("localhost");
        tomcat.setPort(8081);
        tomcat.getConnector();

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
//                String url = "http://localhost?name=" + urlencode(name) + "&lastName=" + urlencode(lastName);
//                "http://client-s.domain-example/path?inputUrl=client-s.domain-example".replace("client-s.domain-example","server1.domain.com");
//                new URL(url);
                /*
                servers
                server1.domain.com
                server2.domain.com
                 */

//                URL.class.newInstance().toURI().
                PrintWriter writer = resp.getWriter();

                String name = req.getParameter("name");
                writer.println("<html><title>Welcome</title><body>");
                writer.println("<h1>" + req.getQueryString() + ", Have a Great Day!</h1>");
                writer.println("</body></html>");
//                URIBuilder uriBuilder = new URIBuilder();
//                uriBuilder.setHost("asdfasd").setScheme().setPort("").addParameter("","").addParameter("dfasdfasd");
//                URIBuilder uriBuilder1 = URIBuilder(url);
//                uriBuilder1.get
            }
        };

        String servletName = "Servlet1";
        String urlPattern = "/go";

        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);

        tomcat.start();
        tomcat.getServer().await();
    }


}
