package com.company.tomcat_server.servlet_service;

public class HTMLFormBuilder {

    public static final String FORM_TAG = "<form method=\"post\" action=\"{{ACTION}}\">";
    public static final String FORM_TAG_CLOSE = "</form>";
    public static final String LABEL_TAG = "<label for=\"{{NAME}}\">";
    public static final String LABEL_TAG_CLOSE = "</label>";
    public static final String INPUT_TEXT_TAG = "<input id=\"{{ID}}\" type=\"text\" name=\"{{NAME}}\">";
    public static final String INPUT_BUTTON_TAG = "<input type=\"submit\" value=\"{{VALUE}}\"/>";
    public static final String NEW_LINE_TAG = "<br>";



    /*
    <form method="post" action="/main">
        <label for="title">Title: </label>
        <input id="title" type="text" name="title"><br><br>

        <label for="author">Author: </label>
        <input id="author" type="text" name="author"><br><br>

        <label for="publishing-date">Publishing date: </label>
        <input id="publishing-date" type="text" name="publishing-date"><br><br>

        <label for="pages">Pages: </label>
        <input id="pages" type="text" name="pages"><br><br>

        <input type="submit" value="Add book" />
    </form>

    */

    public String genLabel(String label, String inputNameParam){
        return LABEL_TAG.replace("{{NAME}}", inputNameParam) +
                label +
                LABEL_TAG_CLOSE;
    }

    public String genButton(String label){
        return INPUT_BUTTON_TAG.replace("{{VALUE}}", label);
    }

    public String genTextField(String id, String name){
        return INPUT_TEXT_TAG.replace("{{ID}}", id).replace("{{NAME}}",name);
    }

    public String genForm(String formContent, String action){
        return FORM_TAG.replace("{{ACTION}}", action)
                + formContent
                + FORM_TAG_CLOSE;
    }

}
