package com.company;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
public class ClientHandler {

    BufferedReader reader;
    PrintWriter out;

    public ClientHandler(BufferedReader in) {
        this.reader = in;
    }

    public String read() throws IOException {
        try {
            String replyMessage = "Waiting for reply...";
            String lineFromServer = "";
            String message = "";
            do {
                lineFromServer = reader.readLine();
                message += lineFromServer + "\n\r";
            } while (!lineFromServer.equals(replyMessage));
            return message;
        } catch (NullPointerException ignored){
            return null;
        }
    }

    public void write() throws IOException {
        String line = reader.readLine();
        out.println(line);
        out.flush();
    }

}
