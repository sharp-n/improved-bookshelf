package com.company;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class TableUtil {


    List<String> columns = new ArrayList<>();

    List<List<String>> rows = new ArrayList<>();

    private final int numberOfColumns;
    private final int numberOfSpacesForPrettyItemsPrinting = 2;

    public TableUtil(List<String> columns, List<List<String>> rows) {
        this.columns = columns;
        this.rows = rows;
        this.numberOfColumns = columns.size();
    }

    public void printTable(){
        validateRows();
        printHeader();
        printBody();
    }

    public void printHeader(){

        for(int i = 0; i<columns.size(); i++){
            int max = countMaxSymbols(columns.get(i),i) ;
            if (i!=0) {
                System.out.print("|");
            }
            printOption(columns.get(i),max);
        }
        System.out.println();
        printLine();
        System.out.println();
    }

    private void printOption(String option, int numberOfSymbols) {
        String pattern = "%-" + numberOfSymbols + "s";
        String column = " =" + option.toUpperCase() + "= ";
        System.out.printf(pattern,column);
    }

    private void printLine() {
        for (int i = 0; i<numberOfColumns;i++){
            int max = countMaxSymbols(columns.get(i),i) ;
            for (int j = 0; j<max;j++){
                System.out.print("-");
            }
            if (i!=numberOfColumns-1) {
                System.out.print("+");
            }
        }
    }

    int countMaxSymbols(String option, int numberOfColumn){
        int max = option.length() + 4;
        for(int i = 0; i<rows.size();i++){
            int lengthOfItem = rows.get(i).get(numberOfColumn).length();
            if (lengthOfItem>max) {
                max = lengthOfItem;
            }
        }
        return max+numberOfSpacesForPrettyItemsPrinting;
    }

    public void printBody(){
        for(int i = 0; i< rows.size(); i++){
            for (int j = 0; j <columns.size(); j++) {
                int max = countMaxSymbols(columns.get(j),j);
                if (j != 0) {
                    System.out.print("|");
                }
                printItem(rows.get(i).get(j),max);
            }
            System.out.println();
        }

    }

    private void printItem(String item, int numberOfSymbols){
        String pattern = "%-" + numberOfSymbols + "s";
        String prettyItem = " " + item + " ";
        System.out.printf(pattern,prettyItem);
    }

    public void validateRows(){
        for(int i = 0; i<rows.size(); i++){
            int rowsSize = rows.get(i).size();
            if(rowsSize<numberOfColumns){
                for (int dif = numberOfColumns - rowsSize; dif > 0; dif--){
                    rows.get(i).add("NULL");
                }
            }
        }
    }
}
