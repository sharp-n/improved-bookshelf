package com.company.server;

import com.company.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket input = serverSocket.accept();
        Scanner in = new Scanner(input.getInputStream());
        PrintWriter out = new PrintWriter(input.getOutputStream());
        Scanner send = new Scanner(System.in);

        ServerHandler.handle(in,out,send);

        out.flush();
        in.close();
        out.close();
        send.close();
        input.close();
        serverSocket.close();

        //out.close();
        //in.close();
        //input.close();
        //serverSocket.close();
    }
}
