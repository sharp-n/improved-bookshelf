package com.company;

import com.company.handlers.ProjectHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger log
            = Logger.getLogger(Main.class);

    static ServerSocket serverSocket;
    public static void main(String[] args) {
            runServer();
            int connections = 0;
            List<Thread> connectionThreads = new ArrayList<>();
            while (connections != 20) {
                Socket input = runInputSocket();
                if (input!=null) {
                    connections++;
                    Thread connectionThread = new Thread(() -> initiateInstruments(input));
                    connectionThread.start();
                    connectionThreads.add(connectionThread);
                }
            }
            joinThreads(connectionThreads);
            stopServer();
    }

    private static void runServer() {
        int port = 8080;
        boolean openPort = false;
        while(!openPort){
            try {
                serverSocket = new ServerSocket(port);
                openPort = true;
            } catch (IOException ioException){
                log.error(ioException.getMessage());
                port++;
            }
        }
    }

    private static Socket runInputSocket() {
        try {
            return serverSocket.accept();
        } catch (IOException ioException){
            log.error(ioException.getMessage());
            return null;
        }
    }

    private static void joinThreads(List<Thread> connectionThreads) {
        try {
            for (Thread connectionThread : connectionThreads) {
                connectionThread.join();
            }
        } catch(InterruptedException interruptedException){
            log.error(interruptedException.getMessage());
        }
    }

    private static void stopServer(){
        try {
            serverSocket.close();
        } catch (IOException ioException){
            log.error(ioException.getMessage());
        }
    }

    private static void initiateInstruments(Socket input){
        try {
            Scanner in = new Scanner(input.getInputStream());
            PrintWriter out = new PrintWriter(input.getOutputStream(),true);
            ProjectHandler serverHandler = new ProjectHandler(in, out);

            serverHandler.handle();

            in.close();
            out.close();

            input.close();
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
    }
}