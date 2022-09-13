package com.company.springbootapp.handlers;

import com.company.User;
import com.company.springbootapp.utils.MainParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChooseMainOptionsHandler {

    MainParams mainParams;

    public void createUser(String name) {
        mainParams.setUser(new User(name));
    }

    public void addTypeOfFileWork(String typeOfWork) {
        mainParams.setTypeOfFileWork(typeOfWork);
    }

    public void addTypeOfItem(String typeOfItem) {
        mainParams.setTypeOfItem(typeOfItem);
    }


}
