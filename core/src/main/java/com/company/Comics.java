package com.company;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Comics extends Item {

    String publishing;

    public Comics(int itemID, String title, int pages, String publishing) {
        super(itemID,title,pages);
        super.borrowed = false;
        this.publishing = publishing;
    }

    public Comics(int itemID, String title, int pages, String publishing, boolean borrowed) {
        super(itemID,title,pages,borrowed);
        this.publishing = publishing;
    }

}