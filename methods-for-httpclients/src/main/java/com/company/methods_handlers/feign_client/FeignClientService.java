package com.company.methods_handlers.feign_client;

import com.company.Item;
import com.company.ParametersForWeb;
import com.company.methods_handlers.MethodsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FeignClientService implements MethodsHandler {

    FeignClientImpl feignClient;

    @Autowired
    public FeignClientService(FeignClientImpl feignClient){
        this.feignClient=feignClient;
    }

    @Override
    public void postForAdd(Item item, ParametersForWeb params) {
        ResponseEntity responseEntity = feignClient.login(params);
    }

    @Override
    public void postForDelete(int id, ParametersForWeb params) {

    }

    @Override
    public void postForTakeOrReturn(int id, ParametersForWeb params, boolean toTake) {

    }

    @Override
    public void postForShow(String parameter, ParametersForWeb params) {

    }

}
