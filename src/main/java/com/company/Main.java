package com.company;

import com.company.handlers.ProjectHandler;
import com.company.server.Server;
import com.company.tomcat_server.MainServletsRunner;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws  InterruptedException {

        Thread telnetThread = new Thread(()-> Server.main(new String[]{}));
        Thread serverThread = new Thread(()-> Server.main(new String[]{}));

        // TODO fix bug on exit from terminal session

        Thread consoleThread = new Thread(()->{
            ProjectHandler handler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out,true));
            handler.handle();
        });

        List<Thread> threads = new ArrayList<>(Arrays.asList(telnetThread,serverThread,consoleThread));
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }

    }

}