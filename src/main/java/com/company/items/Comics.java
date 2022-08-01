package com.company.items;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Comics extends Item {

    String publishing;

    public Comics(int itemID, String title, int pages, String publishing) {
        super.itemID=itemID;
        super.title = title;
        super.pages = pages;
        super.borrowed = false;
        this.publishing = publishing;
    }

}