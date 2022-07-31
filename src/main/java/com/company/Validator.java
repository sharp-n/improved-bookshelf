package com.company;

import com.company.handlers.Librarian;
import lombok.AllArgsConstructor;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Validator {

    PrintWriter out;

    public static final String BAD_NUMBER_VALIDATION_MESSAGE = "ID. It should be a number (>0)"; // FIXME print twice

    public String validateTitle(String title) {
        Pattern pattern = Pattern.compile("[\t|/<>~\\\\]");
        Matcher matcher = pattern.matcher(title);
        if (!Librarian.checkItemForValidity(title) || matcher.find()) {
            printBadValidationMessage("title. It should not contains such symbols as \\t, |, /, <, > ~ or \\");
            return null;
        } else return title;
    }

    public String usernameValidation(String userName) {
        if (userName.equals("exit")) {
            return userName;
        }
        boolean validUserName = User.checkUserNameForValidity(userName);
        if (validUserName) {
            return userName;
        }
        return null;
    }

    public Integer validateID(Integer id) {
        if (id == null) {
            return null;
        }
        if (Librarian.checkItemForValidity(id)) {
            return id;
        }
        printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
        return null;
    }


    public String validateAuthorName(String author) {
        Pattern pattern = Pattern.compile("[@\t#$;:=+*&|/<>?!~()%']");
        Matcher matcher = pattern.matcher(author);
        if (!Librarian.checkItemForValidity(author) || matcher.find()) {
            printBadValidationMessage("author`s name. It should contains only letters, numbers or underscore and spaces");
            return null;
        }
        return author;
    }

    public Integer validatePages(Integer pages) {
        if (pages == null) {
            return null;
        }
        if (!Librarian.checkItemForValidity(pages)) {
            printBadValidationMessage("pages");
            return null;
        }
        if (pages > 5000) {
            printBadValidationMessage("pages. It should be less then 5000");
            return null;
        }
        return pages;
    }

    public Integer validateIdToBorrow(Integer itemID) {
        itemID = validateID(itemID);
        if (itemID == null) {
            return null;
        }
        if (!Librarian.checkItemForValidity(itemID)) {
            printBadValidationMessage(BAD_NUMBER_VALIDATION_MESSAGE);
            return null;
        }
        return itemID;
    }

    public void printBadValidationMessage(String item) {
        out.println("Please, input valid " + item);
    }

}
