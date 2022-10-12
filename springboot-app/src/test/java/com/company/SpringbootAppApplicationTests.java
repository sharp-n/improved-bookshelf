package com.company;

import com.company.db.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional(propagation = Propagation.SUPPORTS)
public class SpringbootAppApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        userRepository.addUser("yana2");
        userRepository.addUser("sof2");
        userRepository.getAllElements().forEach(System.out::println);
    }

}
