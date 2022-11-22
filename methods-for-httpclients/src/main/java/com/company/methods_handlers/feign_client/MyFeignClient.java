package com.company.methods_handlers.feign_client;

import com.company.ParametersForWeb;
import feign.Param;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "feign",url = "http://localhost:8090/")
@Component
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


    @PutMapping("/choose-action/take")
    ResponseEntity borrow(@Param int itemId,
                                 @CookieValue("userName") String userName,
                                 @CookieValue("typeOfWork") String typeOfWork,
                                 @CookieValue("typeOfItem") String typeOfItem);

    @PutMapping("/choose-action/return")
    ResponseEntity returnItem(@Param int id,
                          @CookieValue("userName") String userName,
                          @CookieValue("typeOfWork") String typeOfWork,
                          @CookieValue("typeOfItem") String typeOfItem);

    @PostMapping(value = "/choose-action/show-items")
    String showItems(@CookieValue("userName") String userName,
                            @CookieValue("typeOfWork") String typeOfWork,
                            @CookieValue("typeOfItem") String typeOfItem,
                            String comparator);
}
