package com.company.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8081);
            int connections = 0;
            List<Thread> connectionThreads = new ArrayList<>();
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}