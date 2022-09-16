package com.company;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class ParametersForWeb {

    public String name = "";
    public String typeOfFileWork = "";
    public String typeOfItem = "";

    public void getParametersFromURL(HttpServletRequest req){
        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
    }

}
