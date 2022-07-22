package com.company;

import com.company.server.Server;
import com.company.server.ServerHandler;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws  InterruptedException {

        Thread serverThread = new Thread(()->{
                Server.main(new String[]{});
        });

        // TODO fix bug on exit from terminal session

        Thread consoleThread = new Thread(()->{
            ServerHandler handler = new ServerHandler(new Scanner(System.in), new PrintWriter(System.out,true));
            handler.handle();
        });

        serverThread.start();
        consoleThread.start();
        serverThread.join();
        consoleThread.join();

    }

}