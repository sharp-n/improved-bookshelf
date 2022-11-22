package com.company.methods_handlers.feign_client;

import com.company.ParametersForWeb;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeignClientImpl implements MyFeignClient {

    final MyFeignClient myFeignClient;

//    @Autowired
//    public FeignClientImpl(@Qualifier("com.company.MyFeignClient") MyFeignClient myFeignClient){
//        this.myFeignClient = myFeignClient;
//    }


    @Override
    public ResponseEntity login(ParametersForWeb params) {
        return myFeignClient.login(params);
    }

    @Override
    public ResponseEntity add(String userName, String typeOfWork, String typeOfItem, String item) {
        return myFeignClient.add(userName, typeOfWork, typeOfItem, item);
    }

    @Override
    public ResponseEntity delete(int id, String userName, String typeOfWork, String typeOfItem) {
        return myFeignClient.delete(id, userName, typeOfWork, typeOfItem);
    }

    @Override
    public ResponseEntity borrow(int id, String userName, String typeOfWork, String typeOfItem) {
        return myFeignClient.borrow(id, userName, typeOfWork, typeOfItem);
    }

    @Override
    public ResponseEntity returnItem(int id, String userName, String typeOfWork, String typeOfItem) {
        return myFeignClient.returnItem(id,userName,typeOfWork,typeOfItem);
    }

    @Override
    public String showItems(String userName, String typeOfWork, String typeOfItem, String comparator) {
        return myFeignClient.showItems(userName, typeOfWork, typeOfItem, comparator);
    }
}
