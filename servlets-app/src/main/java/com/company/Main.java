package com.company;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            String webappDirLocation = "src/main/webapp";
            Tomcat tomcat = new Tomcat();

            tomcat.setPort(8081);
            String absolutePath = new File(webappDirLocation).getAbsolutePath();
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", absolutePath);
            System.out.println("configuring app with basedir: " + absolutePath);

            tomcat.start();
            tomcat.getServer().await();

        } catch (LifecycleException exception){
            exception.printStackTrace();
        }
    }

}
