package com.company.methods_handlers.feign_client;

import com.company.ParametersForWeb;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class FeignClientImpl implements MyFeignClient {

    final MyFeignClient myFeignClient;

    @Autowired
    public FeignClientImpl(@Qualifier("com.company.MyFeignClient") MyFeignClient myFeignClient){
        this.myFeignClient = myFeignClient;
    }


    @Override
    public ResponseEntity login(ParametersForWeb params) {
        return myFeignClient.login(params);
    }
}
