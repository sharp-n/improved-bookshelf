package com.company;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Newspaper extends Item {

    public Newspaper(int itemID, String title, int pages) {
        super(itemID,title,pages);
        super.borrowed = false;
    }

    public Newspaper(int itemID, String title, int pages, boolean borrowed) {
        super(itemID,title,pages,borrowed);
    }

}