package com.company.table;

import java.util.ArrayList;
import java.util.List;

public class TableConverter {

    public void fromHtmlToSimpleTable(String htmlTable){

        getRaws(htmlTable);
    }


    public List<List<String>> getRaws(String htmlTable){
        List<List<String>> rows = new ArrayList<>();
        String htmlTableBody = replaceRow(htmlTable);
        while(htmlTableBody.contains("</tr>")){
            String row = htmlTableBody.substring(htmlTable.indexOf("<tr>")+3,htmlTable.indexOf("</tr>"));
            List<String> rowList = new ArrayList<>();
            while(row.contains("</td>")){
                String option = replaceOption(row);

                rowList.add(option);
                row = replaceOption(row);
            }
            rows.add(rowList);
            htmlTableBody = replaceRow(htmlTableBody);
        }
        return rows;
    }

    public List<String> getHeader(String htmlTable){
        List<String> options = new ArrayList<>();
        String htmlTableHeader = htmlTable.substring(htmlTable.indexOf("<tr>"),htmlTable.indexOf("</tr>"));
        while(htmlTableHeader.contains("</th>")){
            String option = htmlTableHeader.substring(htmlTable.indexOf("<th>")+3,htmlTable.indexOf("</th>"));
            options.add(option);
            htmlTableHeader = htmlTableHeader.replace(htmlTableHeader.substring(htmlTable.indexOf("<th>"),htmlTable.indexOf("</th>")),"");
        }
        return options;
    }

    public String replaceRow(String table){
        return table.replace(table.substring(table.indexOf("<tr>"),table.indexOf("</tr>")+4),"");
    }

    public String replaceOption(String table){
        return table.replace(table.substring(table.indexOf("<td>"),table.indexOf("</td>")+4),"");
    }


}
