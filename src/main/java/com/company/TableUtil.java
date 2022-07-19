package com.company;

import java.io.PrintWriter;
import java.util.List;

public class TableUtil {

    List<String> columns;

    List<List<String>> rows;

    PrintWriter out;

    private final int numberOfColumns;
    private static final int NUMBER_OF_SPACES_FOR_PRETTY_ITEMS_PRINTING = 2;
    private static final String CONSTANT_FOR_EMPTY_OPTIONS = "NULL";

    public TableUtil(List<String> columns, List<List<String>> rows, PrintWriter out) {
        this.columns = columns;
        this.rows = rows;
        this.out = out;
        this.numberOfColumns = columns.size();
        validateColumns();
        validateRows();
    }

    public void printTable(){
        printHeader();
        printBody();
    }

    public void printHeader(){

        for(int i = 0; i<columns.size(); i++){
            int max = countMaxSymbols(columns.get(i),i) ;
            if (i!=0) {
                out.print("|");
            }
            printOption(columns.get(i),max);
        }
        out.println();
        printLine();
        out.println();
    }

    private void printOption(String option, int numberOfSymbols) {
        String pattern = "%-" + numberOfSymbols + "s";
        String column = " =" + option.toUpperCase() + "= ";
        out.printf(pattern,column);
    }

    private void printLine() {
        for (int i = 0; i<numberOfColumns;i++){
            int max = countMaxSymbols(columns.get(i),i) ;
            for (int j = 0; j<max;j++){
                out.print("-");
            }
            if (i!=numberOfColumns-1) {
                out.print("+");
            }
        }
    }

    int countMaxSymbols(String option, int numberOfColumn){
        int max = option.length() + 4;
        for (List<String> row : rows) {
            int lengthOfItem = row.get(numberOfColumn).length()+ NUMBER_OF_SPACES_FOR_PRETTY_ITEMS_PRINTING;
            if (lengthOfItem > max) {
                max = lengthOfItem;
            }
        }
        return max;
    }

    public void printBody(){
        for (List<String> row : rows) {
            for (int j = 0; j < columns.size(); j++) {
                int max = countMaxSymbols(columns.get(j), j);
                if (j != 0) {
                    out.print("|");
                }
                printItem(row.get(j), max);
            }
            out.println();
        }

    }

    private void printItem(String item, int numberOfSymbols){
        String pattern = "%-" + numberOfSymbols + "s";
        String prettyItem = " " + item + " ";
        out.printf(pattern,prettyItem);
    }

    public void validateRows(){
        for (List<String> row : rows) {
            int rowsSize = row.size();
            if (rowsSize < numberOfColumns) {
                for (int dif = numberOfColumns - rowsSize; dif > 0; dif--) {
                    row.add(CONSTANT_FOR_EMPTY_OPTIONS);
                }
            }
        }
    }

    private void validateColumns() {
        for (int i = 0; i< columns.size(); i++){
            String column = columns.get(i);
            if (column == null){
                column = CONSTANT_FOR_EMPTY_OPTIONS;
            } else column = column.trim();
            if (column.equals("")){
                column = CONSTANT_FOR_EMPTY_OPTIONS;
            }
            columns.set(i,column);
        }
    }

}
