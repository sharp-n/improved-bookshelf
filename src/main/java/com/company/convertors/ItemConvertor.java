package com.company.convertors;

import com.company.items.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ItemConvertor {

    Item item;

    public String idToString(){
        return Integer.toString(item.getItemID());
    }

    public String titleToString(){
        return item.getTitle().trim();
    }

    public String pagesToString(){
        return Integer.toString(item.getPages());
    }

    public String borrowedToString(){
        return Boolean.toString(item.isBorrowed());
    }

    public List<String> itemToString(){
        List<String> itemAsList = new ArrayList<>();
        itemAsList.add(idToString());
        itemAsList.add(titleToString());
        itemAsList.add(pagesToString());
        itemAsList.add(borrowedToString());
        return itemAsList;
    }

}
