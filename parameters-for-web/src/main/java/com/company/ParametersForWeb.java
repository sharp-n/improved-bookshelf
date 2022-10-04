package com.company;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ParametersForWeb {

    public String name;
    public String typeOfWork;
    public String typeOfItem;

    public ParametersForWeb(){
        name = "";
        typeOfItem = "";
        typeOfWork= "";
    }

    public void getParametersFromURL(HttpServletRequest req){
        name = req.getParameter(ParametersConstants.NAME);
        typeOfWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
    }

}
