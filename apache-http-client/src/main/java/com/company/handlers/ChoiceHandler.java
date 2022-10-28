package com.company.handlers;

import com.company.Validator;
import com.company.enums.ActionsWithItem;
import com.company.enums.MainMenu;
import com.company.handlers.item_handlers.ItemHandler;

import java.io.PrintWriter;
import java.util.Scanner;

public class ChoiceHandler {
    boolean processValue = true;
    MethodsHandler methodsHandler = new MethodsHandler();
    public void getUsersChoice(){

        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in),new PrintWriter(System.out));

        ItemHandler itemHandler = projectHandler.getItemHandler();

        while (processValue) {
            String name = itemHandler.validator.usernameValidation(itemHandler.userInput.usernameInput());
            if(!name.isEmpty()){
                if (name.equals("exit")) {
                    processValue = false;
                } else {
                    processValue = true;
                    System.out.println(itemHandler.initItemsMenuText());
                    Integer itemsChoice = itemHandler.userInput.getItemMenuVar();
                    boolean chosenItem = projectHandler.itemMenuSwitch(MainMenu.getByIndex(itemsChoice));
                    if (chosenItem) {
                        Integer usersChoice = projectHandler.getUsersMainMenuChoice(itemHandler.initActionsWithItemsMenuText(), itemHandler.userInput);
                        if (usersChoice == null) usersChoice = -1;
                        ActionsWithItem actionsWithItem = ActionsWithItem.getByIndex(usersChoice);
                        mainMenuVariants(actionsWithItem, projectHandler);
                    } else {
                        processValue = false;
                    }
                }
            }

        }
    }

    private void mainMenuVariants(ActionsWithItem actionsWithItem, ProjectHandler projectHandler) {
        ItemHandler itemHandler = projectHandler.getItemHandler();
        Librarian librarian = projectHandler.getLibrarian();
        switch (actionsWithItem) {

            case ADD:
                methodsHandler.postForAdd(itemHandler.createItem(itemHandler.getItem(librarian.genItemID())));
                break;

            case DELETE:
                Integer idToDelete = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForDelete(idToDelete);
                break;

            case TAKE:
                Integer idToTake = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForTake(idToTake);
                break;

            case RETURN:
                Integer idToReturn = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForReturn(idToReturn);
                break;

            case SHOW:
                methodsHandler.postForShow(itemHandler);
                break;

            case EXIT_VALUE:
                processValue = false;
                break;

            default:
                break;
        }
    }

}
