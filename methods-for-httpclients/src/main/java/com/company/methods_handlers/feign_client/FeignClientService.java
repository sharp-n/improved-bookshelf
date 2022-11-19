package com.company.methods_handlers.feign_client;

import com.company.Item;
import com.company.ParametersForWeb;
import com.company.methods_handlers.MethodsHandler;
import com.company.table.TableConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

@Service
public class FeignClientService implements MethodsHandler {

    FeignClientImpl feignClient;

    @Autowired
    public FeignClientService(FeignClientImpl feignClient){
        this.feignClient=feignClient;
    }

    @Override
    public void postForAdd(Item item, ParametersForWeb params) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ResponseEntity responseEntity = feignClient.add(gson.toJson(item),params.getName(), params.getTypeOfWork(), params.getTypeOfItem());
        System.out.println(responseEntity.getStatusCode().getReasonPhrase());
    }

    @Override
    public void postForDelete(int id, ParametersForWeb params) {
        ResponseEntity responseEntity = feignClient.delete(id, params.getName(), params.getTypeOfWork(), params.getTypeOfItem());
        System.out.println(responseEntity.getStatusCode().getReasonPhrase());
    }

    @Override
    public void postForTakeOrReturn(int id, ParametersForWeb params, boolean toTake) {
        ResponseEntity responseEntity;
        if(toTake) {
            responseEntity = feignClient.borrow(id, params.getName(), params.getTypeOfWork(), params.getTypeOfItem());
        } else {
            responseEntity = feignClient.returnItem(id, params.getName(), params.getTypeOfWork(), params.getTypeOfItem());
        }
        System.out.println(responseEntity.getStatusCode().getReasonPhrase());
    }

    @Override
    public void postForShow(String comparator, ParametersForWeb params) {
        String responsePage = feignClient.showItems(params.getName(),params.getTypeOfWork(),params.getTypeOfItem(),comparator);
        String table = responsePage.substring(responsePage.indexOf("<table"),responsePage.indexOf("</table>"));
        TableConverter tableConverter = new TableConverter();
        tableConverter.fromHtmlToSimpleTable(table,new PrintWriter(System.out,true));
    }

}
