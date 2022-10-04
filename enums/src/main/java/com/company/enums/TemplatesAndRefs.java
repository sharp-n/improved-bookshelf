package com.company.enums;



import com.company.springappconstants.BlocksNames;

import java.util.Arrays;

public enum TemplatesAndRefs {

    BOOK("book", BlocksNames.ADD_BOOK_FORM,BlocksNames.BOOK_SORTING_OPTIONS_FORM),
    COMICS("comics",BlocksNames.ADD_COMICS_FORM,BlocksNames.COMICS_SORTING_OPTIONS_FORM),
    DEFAULT("default",BlocksNames.ADD_ITEMS_FORM,BlocksNames.ITEMS_SORTING_OPTIONS_FORM);

    private final String addForm;
    private final String sortingForm;

    private final String optionType;


    TemplatesAndRefs(String optionType, String addForm, String sortingForm){
        this.optionType = optionType;
        this.addForm = addForm;
        this.sortingForm = sortingForm;
    }

    public String getAddForm(){
        return addForm;
    }

    public String getSortingForm(){
        return sortingForm;
    }

    public static TemplatesAndRefs getByOptionType(String optionType){
            return Arrays
                    .stream(values())
                    .filter(e -> e.optionType.equalsIgnoreCase(optionType))
                    .findFirst()
                    .orElse(DEFAULT);
    }

}
