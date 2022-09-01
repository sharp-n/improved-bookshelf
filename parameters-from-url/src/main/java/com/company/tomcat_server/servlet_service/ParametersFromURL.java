package com.company.tomcat_server.servlet_service;

import com.company.tomcat_server.constants.ParametersConstants;

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
