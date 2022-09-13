package com.company.springbootapp.utils;

import com.company.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class MainParams {

    User user;
    String typeOfFileWork;
    String typeOfItem;

}
