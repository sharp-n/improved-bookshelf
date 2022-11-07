package com.company.table;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TableConverter {

    private final String RAW_BEGIN = "<tr align=\"center\">";
    private final String RAW_END = "</tr>";
    private final String HEADER_CELL = "<th>";
    private final String HEADER_CELL_END = "</th>";
    private final String BODY_CELL_END = "</td>";
    private final String BODY_CELL = "<td>";

    public void fromHtmlToSimpleTable(String htmlTable, PrintWriter printWriter){
        List<String> header = getHeader(htmlTable);
        List<List<String>> raws = getRaws(htmlTable);
        TableUtil tableUtil = new TableUtil(header,raws,printWriter);
        tableUtil.printTable();
    }


    public List<List<String>> getRaws(String htmlTable){
        List<List<String>> rows = new ArrayList<>();
        String htmlTableBody = replaceRow(htmlTable);
        while(htmlTableBody.contains(RAW_END)){
            String row = htmlTableBody.substring(htmlTableBody.indexOf(RAW_BEGIN)+RAW_BEGIN.length(),htmlTableBody.indexOf(RAW_END));
            List<String> rowList = new ArrayList<>();
            while(row.contains(BODY_CELL_END)){
                String option = row.substring(row.indexOf(BODY_CELL)+BODY_CELL.length(),row.indexOf(BODY_CELL_END));

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
        String htmlTableHeader = htmlTable.substring(htmlTable.indexOf(RAW_BEGIN)+RAW_BEGIN.length(),htmlTable.indexOf(RAW_END));

        while(htmlTableHeader.contains(HEADER_CELL_END)){
            String option = htmlTableHeader.substring(htmlTableHeader.indexOf(HEADER_CELL)+HEADER_CELL.length(),htmlTableHeader.indexOf(HEADER_CELL_END));

            options.add(option);
            htmlTableHeader = htmlTableHeader.replaceFirst(htmlTableHeader.substring(htmlTableHeader.indexOf(HEADER_CELL),htmlTableHeader.indexOf(HEADER_CELL_END)+HEADER_CELL_END.length()),"");

        }
        return options;
    }

    public String replaceRow(String table){
        return table.replaceFirst(table.substring(table.indexOf(RAW_BEGIN),table.indexOf(RAW_END) + RAW_END.length()),"");
    }

    public String replaceOption(String table){
        return table.replaceFirst(table.substring(table.indexOf(BODY_CELL),table.indexOf(BODY_CELL_END)+BODY_CELL_END.length()),"");
    }


}
