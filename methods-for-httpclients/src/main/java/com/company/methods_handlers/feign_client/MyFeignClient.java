package com.company.methods_handlers.feign_client;

import com.company.ParametersForWeb;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feign",url = "http://localhost:8090/")
public interface MyFeignClient {

    @PostMapping("/cookies")
    public ResponseEntity login(@RequestBody ParametersForWeb params);

}
