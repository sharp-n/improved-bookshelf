package com.company.methods_handlers;

import com.company.Item;
import com.company.ParametersForWeb;
import org.springframework.stereotype.Component;

@Component
public interface MethodsHandler {

    void postForAdd(Item item, ParametersForWeb params);

    void postForDelete(int id,ParametersForWeb params);

    void postForTakeOrReturn(int id, ParametersForWeb params,boolean toTake);


    void postForShow(String parameter,ParametersForWeb params);

}
