package com.company.handlers;

import com.company.Librarian;
import com.company.UserInput;
import com.company.Validator;
import com.company.WorkWithFiles;
import com.company.items.Item;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static com.company.Validator.BAD_NUMBER_VALIDATION_MESSAGE;

@NoArgsConstructor
public class ItemHandler<T extends Item> {

    Scanner in;
    PrintWriter out;

    public Librarian librarian;
    public Validator validator;
    public UserInput userInput;

    public ItemHandler(PrintWriter out, Scanner in) {
        this.out = out;
        this.in = in;
        this.validator = new Validator(out);
        this.userInput = new UserInput(out,in);
        this.librarian = new Librarian(new WorkWithFiles(),out);
    }

    // TODO add journal later

    public List<T> getSortedItemsByComparator(List<T> items, Comparator<T> comparator){
        return null;
    }

    T createItem(List<String> options){
        return null;
    }

    public List<String> getItem() throws IOException {
        Integer itemID = validator.validateID(userInput.idUserInput());

        if (itemID == null) {
            validator.printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return null;
        }

        if (librarian.checkIDForExistence(itemID)) {
            out.println("Item with this ID exists. Please change ID and try again");
            return null;
        }

        String title = validator.validateTitle(userInput.titleUserInput());
        if (!Librarian.checkItemForValidity(title)) return null;

        Integer numOfPages = validator.validatePages(userInput.pagesUsersInput());
        if (numOfPages == null) {
            return null;
        }

        return Arrays.asList(itemID.toString(), title, numOfPages.toString());
    }

    public void deleteItem() throws IOException {
        Integer itemID = validator.validateIdToBorrow(userInput.idUserInput());
        if (itemID != null) {
            boolean deleted = librarian.deleteItem(itemID, false);
            if (deleted) {
                userInput.printSuccessMessage("deleted");
            }
        }
    }

    public void initItemBorrowing(boolean borrow) throws IOException {
        Integer itemID = validator.validateIdToBorrow(userInput.idUserInput());
        if (itemID != null) {
            librarian.borrowItem(itemID, borrow);
        }
    }



}
