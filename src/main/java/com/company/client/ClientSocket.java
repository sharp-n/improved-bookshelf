package com.company.client;

import com.company.*;
import jdk.internal.org.jline.utils.WriterOutputStream;

import java.io.*;
import java.net.Socket;

public class ClientSocket {

    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket("localhost", 8080)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

            boolean read = true;

            while (read) {
                ClientHandler clientHandler = new ClientHandler();
                String message = clientHandler.read(in);
                if (message != null) {
                    System.out.println(message);
                    out.flush();
                    clientHandler.write(reader, out);
                } else read = false;
            }
        }
    }

}