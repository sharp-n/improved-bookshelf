package com.company.servlet_service;

import com.company.ParametersConstants;

import javax.servlet.http.HttpServletRequest;


public class ParametersFromURL {

    public String name = "";
    public String typeOfFileWork = "";
    public String typeOfItem = "";

    public void getParametersFromURL(HttpServletRequest req){
        name = req.getParameter(ParametersConstants.NAME);
        typeOfFileWork = req.getParameter(ParametersConstants.TYPE_OF_WORK_WITH_FILE);
        typeOfItem = req.getParameter(ParametersConstants.TYPE_OF_ITEM);
    }

}
