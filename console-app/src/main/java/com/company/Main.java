package com.company;

import com.company.handlers.ProjectHandler;
import org.apache.log4j.Logger;
//import com.company.server.Server;
//import com.company.tomcat_server.MainServletsRunner;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    private static final Logger log
            = Logger.getLogger(Main.class);

    public static void main(String[] args){
        try {//Thread telnetThread = new Thread(()-> Server.main(args));
            //Thread serverThread = new Thread(()-> MainServletsRunner.main(args));

            // TODO fix bug on exit from terminal session

            Thread consoleThread = new Thread(() -> {
                ProjectHandler handler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out, true));
                handler.handle();
            });

            //telnetThread.start();
            //serverThread.start();
            consoleThread.start();
            //telnetThread.join();
            //serverThread.join();
            consoleThread.join();
        } catch (Exception e){
            log.error(e.getMessage() + " : " + Main.class.getSimpleName());
        }
    }

}