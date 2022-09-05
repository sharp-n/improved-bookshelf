package com.company;

import java.io.*;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket("localhost", 8080)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
            PrintStream print = new PrintStream(System.out,true);

            boolean read = true;

            ClientHandler clientHandlerReader = new ClientHandler(in);
            ClientHandler clientHandlerWriter = new ClientHandler(reader, out);

            while (read) {
                String message = clientHandlerReader.read();
                if (message != null) {
                    print.println(message);
                    clientHandlerWriter.write();
                } else read = false;
            }
        }
    }

}