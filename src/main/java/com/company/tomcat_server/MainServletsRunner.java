package com.company.tomcat_server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

public class MainServletsRunner {

    public static void main(String[] args) throws LifecycleException, ServletException {
        String webappDirLocation = "src/main/webapp";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        //String webPort = System.getenv("PORT");
        //if(webPort == null || webPort.isEmpty()) {
        //    webPort = "8080";
        //}

        //tomcat.setPort(Integer.valueOf(webPort));

        tomcat.setPort(8080);
        String absolutePath = new File(webappDirLocation).getAbsolutePath();
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", absolutePath);
        System.out.println("configuring app with basedir: " + absolutePath);
//        tomcat.setBaseDir(Paths.get(webappDirLocation).toAbsolutePath().toString());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
//        File additionWebInfClasses = new File("target/classes");
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, "/src/main/java/org/example/resources",
//                additionWebInfClasses.getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//        tomcat.addContext("index", absolutePath);



        tomcat.start();
        tomcat.getServer().await();
    }

}