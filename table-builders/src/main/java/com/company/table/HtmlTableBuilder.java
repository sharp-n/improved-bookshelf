package com.company.table;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class HtmlTableBuilder {

    List<String> columnsHeaders;

    List<List<String>> rows;

    private static final String TABLE_TAG = "<table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">";
    private static final String TABLE_TAG_CLOSE = "</table>";

    private static final String RAW_TAG_CENTER = "<tr align=\"center\">";
    private static final String RAW_TAG = "<tr>";
    private static final String RAW_TAG_CLOSE = "</tr>";

    private static final String HEADER_CELL_TAG = "<th>";
    private static final String HEADER_CELL_TAG_CLOSE = "</th>";

    private static final String CELL_TAG = "<td>";
    private static final String CELL_TAG_CLOSE = "</td>";

    /*       TEMPLATE
    <tr>
        <th>HEADER1</th>
        <th>HEADER2</th>
    </tr>
    <tr>
        <td>column1 in row1</td>
        <td>column 2 in row1</td>
    </tr>
    <tr>
        <td>column1 in row2</td>
        <td>column 2 in row2</td>
    </tr>
     */

    public String generateTable() {
        return TABLE_TAG +
                generateHeader() +
                generateBody() +
                TABLE_TAG_CLOSE;
    }

    public StringBuilder generateHeader(){
        StringBuilder str = new StringBuilder();
        str.append(RAW_TAG_CENTER);
        for (String header : columnsHeaders) {
            str.append(HEADER_CELL_TAG)
                    .append(header.toUpperCase())
                    .append(HEADER_CELL_TAG_CLOSE);
        }
        str.append(RAW_TAG_CLOSE);
        return str;
    }

    public StringBuilder generateBody(){
        StringBuilder str = new StringBuilder();
        for(List<String> row: rows){
            str.append(RAW_TAG_CENTER);
            for(String cell : row) {
                str.append(CELL_TAG)
                        .append(cell)
                        .append(CELL_TAG_CLOSE);
            }
            str.append(RAW_TAG_CLOSE);
        }
        return str;
    }

}
