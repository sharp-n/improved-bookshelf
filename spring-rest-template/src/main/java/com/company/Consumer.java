package com.company;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class Consumer {

    public static void main(String[] args) {
        Consumer consumer = new Consumer(new RestTemplate());
        consumer.postForTakeMethod(3,new ParametersForWeb("yana","oneFile","Newspaper"));
    }

    RestTemplate restTemplate;

    public void postForTakeMethod(int id, ParametersForWeb params) {
        final String resourceUrl = "http://localhost:8090/choose-action/take";

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "userName=" + params.getName());
        headers.add("Cookie", "typeOfWork="+params.getTypeOfWork());
        headers.add("Cookie", "typeOfItem=" + params.getTypeOfItem());

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

        //System.out.println(response.getBody());
    }

}