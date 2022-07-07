package com.company.client;

import com.company.*;
import com.company.server.ServerHandler;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static com.company.client.ClientHandler.*;

public class ClientSocket {

    private final static int ONE_FILE = 1;
    private final static int TWO_FILES = 2;
    private final static int ADD_BOOK = 1;
    private final static int DELETE_BOOK = 2;
    private final static int TAKE_BOOK = 3;
    private final static int RETURN_BOOK = 4;
    private final static int SHOW_BOOKS = 5;
    private final static int ADD_JOURNAL = 6;
    private final static int DELETE_JOURNAL = 7;
    private final static int TAKE_JOURNAL = 8;
    private final static int RETURN_JOURNAL = 9;
    private final static int SHOW_JOURNALS = 10;
    private final static int EXIT_VALUE = 0;


    public static void main(String[] args) throws IOException {
        boolean filesValue = true;
        boolean value = true;

        Socket socket = new Socket("localhost",8080);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        Scanner in = new Scanner(socket.getInputStream());
        Scanner send = new Scanner(System.in);
/*
        ClientDialogues.setScan(send);

        while (filesValue) {

            sendMessageToServer(out, enterUsername());

            int filesVar = chooseFile();

            switch (filesVar) {
                case ONE_FILE:
                    sendMessageToServer(out,Integer.toString(filesVar));
                    System.out.println("Your items will be saved in one file");
                    break;
                case TWO_FILES:
                    sendMessageToServer(out,Integer.toString(filesVar));
                    System.out.println("Your items will be saved in different files");
                    break;
                case EXIT_VALUE:
                    filesValue = false;
                    value = false;
                    break;
                default:
                    Dialogues.printDefaultMessage();
                    break;
            }

            while (value) {

                System.out.println("\n\t\t0 - Exit" +
                        "\n1 - Add book\t6 - Add journal" +
                        "\n2 - Delete book\t7 - Delete journal" +
                        "\n3 - Take book\t8 - Take journal" +
                        "\n4 - Return book\t9 - Return journal" +
                        "\n5 - Show books\t10 - Show journals");

                int var = Dialogues.getMainMenuVar();
                sendMessageToServer(out,Integer.toString(var));
                switch(var){

                    case ADD_BOOK:
                        sendMessageToServer(out,ClientHandler.makeJsonObjectFromUsersBookInput());
                        System.out.println(in.nextLine());
                        break;

                    case DELETE_BOOK:
                    case DELETE_JOURNAL:
                    case TAKE_BOOK:
                    case RETURN_BOOK:
                    case TAKE_JOURNAL:
                    case RETURN_JOURNAL:
                        sendMessageToServer(out,ClientHandler.enterIdToDoSmth());
                        break;

                    case ADD_JOURNAL:
                        sendMessageToServer(out,ClientHandler.makeJsonObjectFromUsersJournalInput());
                        break;

                    case SHOW_BOOKS:
                        showBooks();
                        break;

                    case SHOW_JOURNALS:
                        showJournals();
                        break;

                    case EXIT_VALUE:
                        value = false;
                        break;

                    default:
                        Dialogues.printDefaultMessage();
                        break;
                }
            }
        }
        in.close();
        send.close();
        out.close();
        socket.close();*/
    }


    private static String enterUsername(){

        boolean validUserName = false;
        String userName = "";
        Dialogues dialogue = new Dialogues();

        while(!validUserName) {
            userName = dialogue.usernameValidation(dialogue.usernameInput());
            if (userName != null) validUserName = true;
        }

        return userName;
    }

}