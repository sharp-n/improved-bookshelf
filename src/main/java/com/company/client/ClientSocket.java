package com.company.client;

import java.io.*;
import java.net.Socket;

public class ClientSocket {

    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket("localhost", 8080)) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());

            boolean read = true;

            ClientHandler clientHandlerReader = new ClientHandler(in);
            ClientHandler clientHandlerWriter = new ClientHandler(reader, out);

            while (read) {
                String message = clientHandlerReader.read();
                if (message != null) {
                    System.out.println(message);
                    out.flush();
                    clientHandlerWriter.write();
                } else read = false;
            }
        }
    }

}