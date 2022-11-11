package com.company;

import com.company.enums.ActionsWithItem;
import com.company.enums.FilesMenu;
import com.company.enums.MainMenu;
import com.company.enums.SortingMenu;
import com.company.handlers.ProjectHandler;
import com.company.handlers.item_handlers.ItemHandler;
import com.company.methods_handlers.ApacheHttpClientMethodsHandler;
import com.company.methods_handlers.MethodsHandler;

import java.io.PrintWriter;
import java.util.Scanner;

public class ChoiceHandler {
    boolean processValue = true;
    MethodsHandler methodsHandler;

    public ChoiceHandler(MethodsHandler methodsHandler) {
        this.methodsHandler = methodsHandler;
    }

    public void getUsersChoice(){

        ProjectHandler projectHandler = new ProjectHandler(new Scanner(System.in), new PrintWriter(System.out,true));

        ItemHandler itemHandler = projectHandler.getItemHandler();

        while (processValue) {
            String name = itemHandler.validator.usernameValidation(itemHandler.userInput.usernameInput());
            if(name!=null) {
                if (name.equals("exit")) {
                    processValue = false;
                } else {
                    processValue = true;
                    int usersFilesMenuChoice = projectHandler.usersFilesMenuChoice(itemHandler.userInput);
                    String typeOfWork = FilesMenu.getByIndex(usersFilesMenuChoice).getServletParameter();
                    while(usersFilesMenuChoice>0) {

                        System.out.println(itemHandler.initItemsMenuText());
                        Integer itemsChoice = itemHandler.userInput.getItemMenuVar();

                        boolean chosenItem = projectHandler.itemMenuSwitch(MainMenu.getByIndex(itemsChoice));
                        if (chosenItem) {
                            Integer usersChoice = projectHandler.getUsersMainMenuChoice(itemHandler.initActionsWithItemsMenuText(), itemHandler.userInput);
                            if (usersChoice == null) usersChoice = -1;
                            ActionsWithItem actionsWithItem = ActionsWithItem.getByIndex(usersChoice);
                            ParametersForWeb params = new ParametersForWeb(name, typeOfWork, MainMenu.getByIndex(itemsChoice).getOption());
                            mainMenuVariants(actionsWithItem, projectHandler, params);
                            name = null;
                        } else {
                            processValue = false;
                            usersFilesMenuChoice = -1;
                        }
                    }
                }
            }
        }
    }

    private void mainMenuVariants(ActionsWithItem actionsWithItem, ProjectHandler projectHandler, ParametersForWeb params) {
        ItemHandler itemHandler = projectHandler.getItemHandler();
        switch (actionsWithItem) {

            case ADD:
                methodsHandler.postForAdd(itemHandler.createItem(itemHandler.getItem(1)),params);
                break;

            case DELETE:
                Integer idToDelete = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForDelete(idToDelete,params);
                break;

            case TAKE:
                Integer idToTake = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForTakeOrReturn(idToTake,params,true);
                break;

            case RETURN:
                Integer idToReturn = new Validator(projectHandler.out).validateID(projectHandler.userInput.idUserInput());
                methodsHandler.postForTakeOrReturn(idToReturn,params,false);
                break;

            case SHOW:
                int parameter = projectHandler.userInput.getSortingVar(itemHandler.genSortingMenuText());
                String paramStr = SortingMenu.getByIndex(parameter).getDbColumn();
                methodsHandler.postForShow(paramStr,params);
                break;

            case EXIT_VALUE:
                processValue = false;
                break;

            default:
                break;
        }
    }

}
