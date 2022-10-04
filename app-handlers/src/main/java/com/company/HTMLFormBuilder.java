package com.company;

public class HTMLFormBuilder {

    public static final String NAME_TEMPLATE = "{{NAME}}";
    public static final String VALUE_TEMPLATE = "{{VALUE}}";
    public static final String ACTION_TEMPLATE = "{{ACTION}}";
    public static final String ID_TEMPLATE = "{{ID}}";

    public static final String FORM_TAG = "<form method=\"post\" action=\"" + ACTION_TEMPLATE + "\">";
    public static final String FORM_TAG_CLOSE = "</form>";
    public static final String LABEL_TAG = "<label for=\"" + NAME_TEMPLATE +"\">";
    public static final String LABEL_TAG_CLOSE = "</label>";
    public static final String INPUT_TEXT_TAG = "<input id=\"" + ID_TEMPLATE + "\" type=\"text\" name=\"" + NAME_TEMPLATE + "\">";
    public static final String INPUT_BUTTON_TAG = "<input type=\"submit\" value=\"" + VALUE_TEMPLATE + "\"/>";
    public static final String RADIO_BUTTON_TAG = "<input type=\"radio\" name=\"" + NAME_TEMPLATE + "\" value=\"" + VALUE_TEMPLATE + "\"/>";
    public static final String NEW_LINE_TAG = "<br>";

    public String genLabel(String label, String inputNameParam){
        return LABEL_TAG.replace(NAME_TEMPLATE, inputNameParam) +
                label +
                LABEL_TAG_CLOSE;
    }

    public String genButton(String label){
        return INPUT_BUTTON_TAG.replace(VALUE_TEMPLATE, label);
    }

    public String genTextField(String id, String name){
        return INPUT_TEXT_TAG.replace(ID_TEMPLATE, id).replace(NAME_TEMPLATE,name);
    }

    public String genForm(String formContent, String action){
        return FORM_TAG.replace(ACTION_TEMPLATE, action)
                + formContent
                + FORM_TAG_CLOSE;
    }

    public String genRadioButton(String name, String value, String label){
        return RADIO_BUTTON_TAG
                .replace(NAME_TEMPLATE,name)
                .replace(VALUE_TEMPLATE, value)
                + label;
    }

}
