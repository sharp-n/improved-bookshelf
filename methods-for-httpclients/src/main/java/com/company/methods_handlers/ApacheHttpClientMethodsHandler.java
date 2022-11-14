package com.company.methods_handlers;

import com.company.Item;
import com.company.ParametersForWeb;
import com.company.table.TableConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class ApacheHttpClientMethodsHandler implements MethodsHandler {

    private static final Logger log
            = Logger.getLogger(ApacheHttpClientMethodsHandler.class);


    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    HttpClient httpClient = HttpClientBuilder.create()
            //.setDefaultCookieStore(basicCookieStore)
            .build();

    public void setCookies(HttpPost request, ParametersForWeb params){
        request.addHeader("Cookie","userName=" + params.getName());
        request.addHeader("Cookie","typeOfWork=" + params.getTypeOfWork());
        request.addHeader("Cookie","typeOfItem=" + params.getTypeOfItem());
    }

    public void postForAdd(Item item, ParametersForWeb params){
        try{
            HttpPost request = new HttpPost("http://localhost:8090/choose-action/add");
            request.setHeader("Accept", "text/html");
            request.setHeader("Content-type", "application/json");

            String paramsGson =  gson.toJson(item);
            StringEntity json = new StringEntity(paramsGson, ContentType.APPLICATION_JSON);
            request.setEntity(json);

            setCookies(request,params);

            HttpResponse response = httpClient.execute(request);
            System.out.println(EntityUtils.toString(response.getEntity()));

        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
    }

    public BasicCookieStore createCookiesAndBasicCookieStore(ParametersForWeb params,String path){
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        BasicClientCookie cookieUserName = new BasicClientCookie("userName",params.getName());
        BasicClientCookie cookieTypeOfItem = new BasicClientCookie("typeOfItem",params.getTypeOfItem());
        BasicClientCookie cookieTypeOfWork = new BasicClientCookie("typeOfWork",params.getTypeOfWork());
        cookieUserName.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
        cookieTypeOfItem.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
        cookieTypeOfWork.setAttribute(ClientCookie.DOMAIN_ATTR, "true");

        basicCookieStore.addCookie(cookieUserName);
        basicCookieStore.addCookie(cookieTypeOfItem);
        basicCookieStore.addCookie(cookieTypeOfWork);

        return basicCookieStore;
    }

    public void createNewRequestWithIdParameter(String path, int id,ParametersForWeb params) {
        try {
            NameValuePair nameValuePair = new BasicNameValuePair("item_id", Integer.toString(id));
            HttpPost request = new HttpPost("http://localhost:8090" + path);
            request.setHeader("Accept", "text/html");
            request.setEntity(new UrlEncodedFormEntity(new ArrayList<>(Collections.singletonList(nameValuePair))));
            setCookies(request, params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException ioException) {
            log.error(ioException.getMessage() + " : " + ApacheHttpClientMethodsHandler.class.getSimpleName() + " : createNewRequestWithIdParameter()");
        }
    }

    public void postForDelete(int id,ParametersForWeb params){
        createNewRequestWithIdParameter("/choose-action/delete",id,params);
    }

    public void postForTakeOrReturn(int id, ParametersForWeb params,boolean toTake) {
        if(toTake){
            createNewRequestWithIdParameter("/choose-action/take", id, params);
        } else {
            createNewRequestWithIdParameter("/choose-action/return", id, params);
        }

    }

    public void postForShow(String parameter,ParametersForWeb params){
        try {
            HttpPost request = new HttpPost("http://localhost:8090/choose-action/show");
            NameValuePair nameValuePair = new BasicNameValuePair("sorting-param",parameter);
            request.setEntity(new UrlEncodedFormEntity(new ArrayList<>(Collections.singletonList(nameValuePair))));
            setCookies(request,params);
            HttpResponse response = httpClient.execute(request);
            String responsePage = EntityUtils.toString(response.getEntity());
            String table = responsePage.substring(responsePage.indexOf("<table"),responsePage.indexOf("</table>"));
            TableConverter tableConverter = new TableConverter();
            tableConverter.fromHtmlToSimpleTable(table,new PrintWriter(System.out,true));
        } catch (IOException ioException) {
            log.error(ioException.getMessage() + " : " + ApacheHttpClientMethodsHandler.class.getSimpleName() + " : postForShow()");
        }
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
