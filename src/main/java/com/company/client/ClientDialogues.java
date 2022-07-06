package com.company.client;

import com.company.Dialogues;
import com.company.User;

import java.util.InputMismatchException;
public class ClientDialogues extends Dialogues {

    @Override
    public String usernameInput() {
        System.out.println("\nInput your name. If you want to use default folders(s) write \"default\"");
        return scan.nextLine().trim();
    }

    @Override
    public String usernameValidation(String userName){
        boolean validUserName = User.checkUserNameForValidity(userName);
        if (validUserName) {
            return userName;
        }
        return null;
    }

    public static int getMainMenuVar(){
        try {
            return scan.nextInt();
        } catch(InputMismatchException e) {
            return 0;
        }
    }
}
