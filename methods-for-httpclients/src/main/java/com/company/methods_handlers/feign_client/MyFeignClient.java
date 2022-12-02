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

//    @PostMapping(value = "/cookies", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity login(@Param("params") ParametersForWeb params);

    @PutMapping(value = "/choose-action/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity add(@Param String item,
                              @CookieValue("userName") String userName,
                              @CookieValue("typeOfWork") String typeOfWork,
                              @CookieValue("typeOfItem") String typeOfItem);

    @DeleteMapping(value = "/choose-action/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity delete(@Param int id,
                                 @CookieValue("userName") String userName,
                                 @CookieValue("typeOfWork") String typeOfWork,
                                 @CookieValue("typeOfItem") String typeOfItem);

    @PutMapping(value = "/choose-action/take", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity borrow(@Param int itemId,
                                 @CookieValue("userName") String userName,
                                 @CookieValue("typeOfWork") String typeOfWork,
                                 @CookieValue("typeOfItem") String typeOfItem);

    @PutMapping(value = "/choose-action/return", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity returnItem(@Param int id,
                          @CookieValue("userName") String userName,
                          @CookieValue("typeOfWork") String typeOfWork,
                          @CookieValue("typeOfItem") String typeOfItem);

    @PostMapping(value = "/choose-action/show-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    String showItems(@CookieValue("userName") String userName,
                     @CookieValue("typeOfWork") String typeOfWork,
                     @CookieValue("typeOfItem") String typeOfItem,
                     @Param("comparator") String comparator);

}
