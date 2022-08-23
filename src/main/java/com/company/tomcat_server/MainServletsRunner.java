package com.company.tomcat_server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;

public class MainServletsRunner {

    public static void main(String[] args) {
        try {
            String webappDirLocation = "src/main/webapp";
            Tomcat tomcat = new Tomcat();

            tomcat.setPort(8080);
            String absolutePath = new File(webappDirLocation).getAbsolutePath();
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", absolutePath);
            System.out.println("configuring app with basedir: " + absolutePath);

            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException | ServletException exception){
            exception.printStackTrace();
        }
    }

}
