package com.company.methods_handlers;

import com.company.Item;
import com.company.ParametersForWeb;
import com.company.table.TableConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

@AllArgsConstructor
public class RestTemplateHttpClientMethodsHandler implements MethodsHandler {

    private static final Logger log
            = Logger.getLogger(RestTemplateHttpClientMethodsHandler.class);

//    public static void main(String[] args) {
//        Consumer consumer = new Consumer(new RestTemplate());
//        consumer.postForTakeMethod(3,new ParametersForWeb("yana","oneFile","Newspaper"));
//    }
    final private String urlChooseAction = "http://localhost:8090/choose-action";

    RestTemplate restTemplate;

    public void setCookies(ParametersForWeb params, HttpHeaders headers){
        headers.add("Cookie", "userName=" + params.getName());
        headers.add("Cookie", "typeOfWork="+params.getTypeOfWork());
        headers.add("Cookie", "typeOfItem=" + params.getTypeOfItem());
    }

    @Override
    public void postForAdd(Item item, ParametersForWeb params) {
        try {
            final String resourceUrl = urlChooseAction + "/add";
            HttpHeaders headers = new HttpHeaders();
            setCookies(params, headers);
            headers.setAccept( Collections.singletonList(MediaType.TEXT_HTML));
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            HttpEntity<Item> httpEntity = new HttpEntity<>(item,headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    resourceUrl,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);
            HttpStatus statusCode = response.getStatusCode();
            System.out.println(statusCode.is2xxSuccessful() || statusCode.is3xxRedirection());
            System.out.println(response.getBody());
        } catch (Exception exception){
            log.error(exception);
        }
    }

    @Override
    public void postForDelete(int id, ParametersForWeb params) {
        doRequest(urlChooseAction + "/show", id,params);
    }

    @Override
    public void postForTakeOrReturn(int id, ParametersForWeb params, boolean toTake) {
        String resourceUrl;
        if(toTake){
             resourceUrl = urlChooseAction + "/take";
        } else {
            resourceUrl = urlChooseAction + "/return";
        }
        doRequest(resourceUrl,id,params);
    }

    @Override
    public void postForShow(String parameter, ParametersForWeb params) {
        String resourceUrl = urlChooseAction + "/show";
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        setCookies(params,headers);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("sorting-param", parameter);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        //ResponseEntity<String> response = restTemplate.postForEntity(
        //        resourceUrl, request , String.class);

        ResponseEntity<String> response = restTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                request,
                String.class);
        HttpStatus statusCode = response.getStatusCode();
        System.out.println(statusCode.is2xxSuccessful() || statusCode.is3xxRedirection());
        String tableHtml = response.getBody();
        TableConverter tableConverter = new TableConverter();
        tableConverter.fromHtmlToSimpleTable(tableHtml,new PrintWriter(System.out,true));
    }

    public void doRequest(String resourceUrl, int id, ParametersForWeb params){
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        setCookies(params,headers);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("item_id", Integer.toString(id));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        //ResponseEntity<String> response = restTemplate.postForEntity(
        //        resourceUrl, request , String.class);

        ResponseEntity<Void> response = restTemplate.exchange(
                resourceUrl,
                HttpMethod.POST,
                request,
                Void.class);
        HttpStatus statusCode = response.getStatusCode();
        System.out.println(statusCode.is2xxSuccessful() || statusCode.is3xxRedirection());
    }

}