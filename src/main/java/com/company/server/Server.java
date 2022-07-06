package com.company.server;

import lombok.var;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) throws IOException {

        CompletableFuture.runAsync(()->{
            while(true){
                try{
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        var threads = new ArrayList<Thread>();
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            for(int i=0;i<5;i++){
                var t = new Thread(()->{
                    try(Socket input = serverSocket.accept();){
                        Scanner in = new Scanner(input.getInputStream());
                        PrintWriter out = new PrintWriter(input.getOutputStream());
                        ServerHandler serverHandler = new ServerHandler(in, out);

                        serverHandler.handle();
                    } catch(Exception ignored){

                    }
                });
                t.start();
                threads.add(t);
                //Scanner send = new Scanner(System.in);
            }
            threads.forEach(t-> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        /*
        in.close();
        out.close();
        //send.close();
        input.close();
        serverSocket.close();
*/
        //out.close();
        //in.close();
        //input.close();
        //serverSocket.close();
    }
}
