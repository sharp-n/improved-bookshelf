package com.company.tomcat_server.servlet_service;

import com.company.tomcat_server.constants.TemplatesConstants;

public class HTMLFormBuilder {

    public static final String FORM_TAG = "<form method=\"post\" action=\"" + TemplatesConstants.ACTION_TEMPLATE + "\">";
    public static final String FORM_TAG_CLOSE = "</form>";
    public static final String LABEL_TAG = "<label for=\"" + TemplatesConstants.NAME_TEMPLATE +"\">";
    public static final String LABEL_TAG_CLOSE = "</label>";
    public static final String INPUT_TEXT_TAG = "<input id=\"" + TemplatesConstants.ID_TEMPLATE + "\" type=\"text\" name=\"" + TemplatesConstants.NAME_TEMPLATE + "\">";
    public static final String INPUT_BUTTON_TAG = "<input type=\"submit\" value=\"" + TemplatesConstants.VALUE_TEMPLATE + "\"/>";
    public static final String RADIO_BUTTON_TAG = "<input type=\"radio\" name=\"" + TemplatesConstants.NAME_TEMPLATE + "\" value=\"" + TemplatesConstants.VALUE_TEMPLATE + "\"/>";
    public static final String NEW_LINE_TAG = "<br>";

    public String genLabel(String label, String inputNameParam){
        return LABEL_TAG.replace(TemplatesConstants.NAME_TEMPLATE, inputNameParam) +
                label +
                LABEL_TAG_CLOSE;
    }

    public String genButton(String label){
        return INPUT_BUTTON_TAG.replace(TemplatesConstants.VALUE_TEMPLATE, label);
    }

    public String genTextField(String id, String name){
        return INPUT_TEXT_TAG.replace(TemplatesConstants.ID_TEMPLATE, id).replace(TemplatesConstants.NAME_TEMPLATE,name);
    }

    public String genForm(String formContent, String action){
        return FORM_TAG.replace(TemplatesConstants.ACTION_TEMPLATE, action)
                + formContent
                + FORM_TAG_CLOSE;
    }

    public String genRadioButton(String name, String value, String label){
        return RADIO_BUTTON_TAG
                .replace(TemplatesConstants.NAME_TEMPLATE,name)
                .replace(TemplatesConstants.VALUE_TEMPLATE, value)
                + label;
    }

}
