package com.company;

public class Main {

    public static void main(String[] args) {

        boolean value = true;
        while (value) {

            System.out.println("\n1 - Add book\t6 - Add journal\n" +
                    "2 - Delete book\t7 - Delete journal\n" +
                    "3 - Take book\t8 - Take journal\n" +
                    "4 - Return book\t9 - Return journal\n" +
                    "5 - Show books\t10 - Show journals\n" +
                    "\t\t11 - Exit");

            int var = Dialogues.getMainMenuVar();
            String booksFilePath = Book.getFilePath();
            String journalsFilePath = Journal.getFilePath();
            value = switch (var) {
                case 1 -> Dialogues.addingDialogue(booksFilePath);

                case 2 -> Dialogues.deletingDialogue(booksFilePath);

                case 3 -> Dialogues.takingDialogue(booksFilePath);

                case 4 -> Dialogues.returningDialogue(booksFilePath);

                case 5 -> Dialogues.sortingDialogue(booksFilePath);

                case 6 -> Dialogues.addingDialogue(journalsFilePath);

                case 7 -> Dialogues.deletingDialogue(journalsFilePath);

                case 8 -> Dialogues.takingDialogue(journalsFilePath);

                case 9 -> Dialogues.returningDialogue(journalsFilePath);

                case 10 -> Dialogues.sortingDialogue(journalsFilePath);

                case 11 -> false;

                default -> Dialogues.printDefaultMessage();
            };
        }
    }
}