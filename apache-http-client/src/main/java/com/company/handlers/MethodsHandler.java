package com.company.handlers;

import com.company.Item;
import com.company.handlers.item_handlers.ItemHandler;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class MethodsHandler {


    final CloseableHttpClient httpClient = HttpClients.createDefault();

    public void simpleGet() throws IOException, ParseException {
        HttpGet request = new HttpGet("http://localhost:8090/login");
        CloseableHttpResponse response = httpClient.execute(request);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    public void postForAdd(Item item){
//        try{
//
//        } catch (IOException ioException) {
//
//        }
    }

    public void postForDelete(int id){
//        try {
//
//        } catch (IOException ioException) {
//
//        }
    }

    public void postForTake(int id) {
        try {
            HttpPost request = new HttpPost("http://localhost:8090/choose-action/take");
            request.setHeader("Accept", "text/html");
            StringEntity entity = new StringEntity("46");
            request.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(request);
            System.out.println(request.getEntity());
        } catch (IOException ioException) {

        }
    }

    public void postForReturn(int id){
//        try {
//
//        } catch (IOException ioException) {
//
//        }
    }

    public void postForShow(ItemHandler itemHandler){
//        try {
//
//        } catch (IOException ioException) {
//
//        }
    }
    // Error 406
//    public void simplePost(ParametersForWeb params) throws IOException, ParseException {
//        try {
//            HttpPost request = new HttpPost("http://localhost:8090/login");
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String paramsGson =  gson.toJ son(params, ParametersForWeb.class);
//            System.out.println(paramsGson);
//            StringEntity json = new StringEntity(paramsGson, ContentType.APPLICATION_JSON);
//            request.setEntity(json);
//            request.setHeader("Accept", "text/html");
//            request.setHeader("Content-type", "application/json");
//            CloseableHttpResponse response = httpClient.execute(request);
//            System.out.println(EntityUtils.toString(response.getEntity()));
//        } catch (Exception e){
//            e.printStackTrace();
//        }


}
