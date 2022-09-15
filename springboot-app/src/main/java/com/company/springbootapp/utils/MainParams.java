package com.company.springbootapp.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class MainParams {

    String name = "";
    String typeOfFileWork = "";
    String typeOfItem = "";

}
