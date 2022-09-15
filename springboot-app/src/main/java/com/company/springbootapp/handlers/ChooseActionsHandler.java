package com.company.springbootapp.handlers;

import com.company.springbootapp.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ChooseActionsHandler {

    Map<String,String> typeOfItemTemplate = new HashMap<>();
    Map<String,String> typeOfItemRef = new HashMap<>();
    CookieUtil cookieUtil;
    public String getAddTemplateBySimpleCLassName(String typeOfItem){ //todo refactor map init
        typeOfItemTemplate.put("book","add-book-form");
        typeOfItemTemplate.put("comics","add-comics-form");
        typeOfItemTemplate.put("default","add-main-option-items-form");
        for (Map.Entry<String,String> typeOfItemTemplateEntry : typeOfItemTemplate.entrySet()) {
            if (typeOfItemTemplateEntry.getKey().equalsIgnoreCase(typeOfItem)){
                return typeOfItemTemplateEntry.getValue();
            }
        }
        return typeOfItemTemplate.get("default");
    }

    public String redirectionToAddPage(String typeOfItem) { // todo optimize (include getTemplateBySimpleCLassName())
        typeOfItemRef.put("book","/add/book"); // todo refactor map init
        typeOfItemRef.put("comics","/add/comics");
        typeOfItemRef.put("default","/add");
        for (Map.Entry<String,String> typeOfItemRefEntry : typeOfItemRef.entrySet()) {
            System.out.println(typeOfItemRefEntry.getKey());
            if (typeOfItemRefEntry.getKey().equalsIgnoreCase(typeOfItem)){
                System.out.println(typeOfItemRefEntry.getValue());
                return "redirect:/choose-action" + typeOfItemRefEntry.getValue();
            }
        }
        System.out.println(typeOfItemRef.get("default"));
        return "redirect:/choose-action" + typeOfItemRef.get("default");
    }


    public String getSortingTemplateByTypeOfItem(String typeOfItem) { // todo optimize
        typeOfItemTemplate.put("book","books-sorting-options-form");
        typeOfItemTemplate.put("comics","comics-sorting-options-form");
        typeOfItemTemplate.put("default","items-sorting-options-form");
        for (Map.Entry<String,String> typeOfItemTemplateEntry : typeOfItemTemplate.entrySet()) {
            if (typeOfItemTemplateEntry.getKey().equalsIgnoreCase(typeOfItem)){
                return typeOfItemTemplateEntry.getValue();
            }
        }
        return typeOfItemTemplate.get("default");
    }

    public void addItem(List<String> itemOptions) {




    }
}
