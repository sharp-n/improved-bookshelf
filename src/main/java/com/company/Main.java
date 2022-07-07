package com.company;

import com.company.server.ServerHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerHandler handler = new ServerHandler(new Scanner(System.in),new PrintWriter(System.out));
        handler.handle();
    }

}