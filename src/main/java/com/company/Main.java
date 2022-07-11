package com.company;

import com.company.server.Server;
import com.company.server.ServerHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread serverThread = new Thread(()->{
            try {
                //if (args[0].equals("server")) {
                    Server.main(new String[]{});
                //} else {
                 //   ServerHandler handler = new ServerHandler(new Scanner(System.in), new PrintWriter(System.out));
                //    handler.handle();
                //}
            }catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consoleThread = new Thread(()->{
            ServerHandler handler = new ServerHandler(new Scanner(System.in), new PrintWriter(System.out));
            try {
                handler.handle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
        consoleThread.start();
        serverThread.join();
        consoleThread.join();

    }

}