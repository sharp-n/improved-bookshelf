package com.company.springbootapp.handlers;

import com.company.User;
import com.company.springbootapp.utils.MainParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class ChooseMainOptionsHandler {

    MainParams mainParams;

    public void createUser(String name) {
        mainParams.setName(name);
    }

    public void addTypeOfFileWork(String typeOfWork) {
        mainParams.setTypeOfFileWork(typeOfWork);
    }

    public void addTypeOfItem(String typeOfItem) {
        mainParams.setTypeOfItem(typeOfItem);
    }


}
