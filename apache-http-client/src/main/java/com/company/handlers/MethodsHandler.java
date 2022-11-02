package com.company.handlers;

import com.company.Item;
import com.company.ParametersForWeb;
import com.company.handlers.item_handlers.ItemHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MethodsHandler {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    HttpClient httpClient;
    BasicCookieStore basicCookieStore;

    public void simpleGet() throws IOException, ParseException {
        HttpGet request = new HttpGet("http://localhost:8090/login");
        HttpResponse response = httpClient.execute(request);

    }

    public void loginPost(ParametersForWeb params){
        try {
            HttpPost request = new HttpPost("http://localhost:8090/login");
            String paramsGson =  gson.toJson(params, ParametersForWeb.class);
            System.out.println(paramsGson);
            StringEntity json = new StringEntity(paramsGson, ContentType.APPLICATION_JSON);
            request.setEntity(json);

            basicCookieStore = createCookiesAndBasicCookieStore(params, "/*");

            httpClient = HttpClientBuilder.create()
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .build();

            request.setHeader("Accept", "text/html");
            request.setHeader("Content-type", "application/json");
            HttpContext basicHttpContext = new BasicHttpContext();
            basicHttpContext.setAttribute(HttpClientContext.COOKIE_STORE, basicCookieStore);
//            request.addHeader("Cookie","userName=" + params.getName());
//            request.addHeader("Cookie","typeOfWork=" + params.getTypeOfWork());
//            request.addHeader("Cookie","typeOfItem=" + params.getTypeOfItem());

            HttpResponse response = httpClient.execute(request,basicHttpContext);

            System.out.println(response);
        //    CloseableHttpResponse response = httpClient.execute(request);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void postForAdd(Item item){
//        try{
//
//        } catch (IOException ioException) {
//
//        }
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

    public void getForDelete(ParametersForWeb params){
        try {

            HttpPost request = new HttpPost("http://localhost:8090/choose-action/delete");
            request.setHeader("Accept", "text/html");

            request.setHeader("Set-Cookie","userName=" + params.getName());
            request.setHeader("Set-Cookie","typeOfWork=" + params.getTypeOfWork());
            request.setHeader("Set-Cookie","typeOfItem=" + params.getTypeOfItem());

            //BasicCookieStore basicCookieStore = createCookiesAndBasicCookieStore(params,"/choose-action/delete");

//            HttpContext localContext = new BasicHttpContext();
//            localContext.setAttribute(HttpClientContext.COOKIE_STORE, basicCookieStore);

//            request.setHeader("Set-Cookie","userName=" + params.getName());
//            request.setHeader("Set-Cookie","typeOfWork=databaseMySQL" + params.getTypeOfWork());
//            request.setHeader("Set-Cookie","typeOfItem=Newspaper" + params.getTypeOfItem());

            HttpResponse response = httpClient.execute(request);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }
    public void postForDelete(int id,ParametersForWeb params){
        try {

            BasicCookieStore basicCookieStore = createCookiesAndBasicCookieStore(params,"/choose-action/delete");

//            CloseableHttpClient httpClient = HttpClientBuilder.create()
//                    //.setDefaultCookieStore(new BasicCookieStore())
//                    .build();

            HttpPost request = new HttpPost("http://localhost:8090/choose-action/delete");
            request.setHeader("Accept", "text/html");
            StringEntity entity = new StringEntity(Integer.toString(id));
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(HttpClientContext.COOKIE_STORE, basicCookieStore);
            request.setEntity(entity);

//            request.setHeader("Set-Cookie","userName=" + params.getName());
//            request.setHeader("Set-Cookie","typeOfWork=databaseMySQL" + params.getTypeOfWork());
//            request.setHeader("Set-Cookie","typeOfItem=Newspaper" + params.getTypeOfItem());

            HttpResponse response = httpClient.execute(request,localContext);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException ioException) {
            ioException.printStackTrace();
        }
    }

    public void postForTake(int id) {
        try {
            HttpPost request = new HttpPost("/choose-action/take");
            request.setHeader("Accept", "text/html");
            StringEntity entity = new StringEntity(Integer.toString(id));
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.getEntity());
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
