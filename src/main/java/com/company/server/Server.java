package com.company.server;

import lombok.var;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(8080);
        int connections = 0;
        var connectionThreads = new ArrayList<Thread>();
        while (connections != 20) {
            Socket input = serverSocket.accept();
            connections++;
            Thread connectionThread = new Thread(() -> {
                try {
                    Scanner in = new Scanner(input.getInputStream());
                    PrintWriter out = new PrintWriter(input.getOutputStream());
                    ServerHandler serverHandler = new ServerHandler(in, out);

                    serverHandler.handle();
                    in.close();
                    out.close();
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            connectionThread.start();
            connectionThreads.add(connectionThread);
        }
        for (Thread connectionThread : connectionThreads) {
            connectionThread.join();
        }
        serverSocket.close();

    }

}