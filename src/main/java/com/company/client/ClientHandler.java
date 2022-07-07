package com.company.client;

import com.company.Book;
import com.company.Dialogues;
import com.company.Journal;
import com.company.Librarian;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientHandler {

    public String read(BufferedReader in) throws IOException {
        try {
            String replyMessage = "Waiting for reply...";
            String lineFromServer = "";
            String message = "";
            do {
                lineFromServer = in.readLine();
                message += lineFromServer + "\n";
            } while (!lineFromServer.equals(replyMessage));
            return message;
        } catch (NullPointerException ignored){
            return null;
        }
    }

    public void write(BufferedReader reader, PrintWriter out) throws IOException {
        String line = reader.readLine();
        out.println(line);
        out.flush();
    }

}
