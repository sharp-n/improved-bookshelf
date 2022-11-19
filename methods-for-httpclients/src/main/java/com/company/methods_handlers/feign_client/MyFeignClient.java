package com.company.methods_handlers.feign_client;

import com.company.ParametersForWeb;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "feign",url = "http://localhost:8090/")
public interface MyFeignClient {

    @PostMapping("/cookies")
    public ResponseEntity login(@RequestBody ParametersForWeb params);

    @PutMapping("/choose-action/add")
    ResponseEntity add(String item,
                              @CookieValue("userName") String userName,
                              @CookieValue("typeOfWork") String typeOfWork,
                              @CookieValue("typeOfItem") String typeOfItem);

    @DeleteMapping("/choose-action/delete")
    ResponseEntity delete(int id,
                                 @CookieValue("userName") String userName,
                                 @CookieValue("typeOfWork") String typeOfWork,
                                 @CookieValue("typeOfItem") String typeOfItem);


    @PutMapping("/take")
    ResponseEntity borrow(int id,
                                 @CookieValue("userName") String userName,
                                 @CookieValue("typeOfWork") String typeOfWork,
                                 @CookieValue("typeOfItem") String typeOfItem);

    @PutMapping("/return")
    ResponseEntity returnItem(int id,
                          @CookieValue("userName") String userName,
                          @CookieValue("typeOfWork") String typeOfWork,
                          @CookieValue("typeOfItem") String typeOfItem);

    @PostMapping(value = "/show-items")
    String showItems(@CookieValue("userName") String userName,
                            @CookieValue("typeOfWork") String typeOfWork,
                            @CookieValue("typeOfItem") String typeOfItem,
                            @RequestParam(name = "sorting-param") String comparator);
}
