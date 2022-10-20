package com.company;

import com.company.db.entities.User;
import com.company.db.repositories.UserRepository;
import com.company.db.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTests {

    private final String USER_NAME = "test user";
    private final Integer USER_ID = 1;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void addUserTest() {
        userService.addUser(USER_NAME);
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        String provided = users.stream().filter(user -> user.getName().equals(USER_NAME)).findFirst().get().getName();
        Assertions.assertEquals(USER_NAME,provided);
        deleteUserForTest(USER_ID);
    }

    @Test
    void getUserByIDTest(){
        saveUserForTest(USER_NAME);
        Integer provided = userRepository.findAll().stream().filter(user -> user.getName().equals(USER_NAME)).findFirst().get().getId();
        Assertions.assertEquals(USER_NAME,userService.getUserByID(provided).getName());
        deleteUserForTest(provided);
    }

    @Test
    void removeUserTest(){
        saveUserForTest(USER_NAME);
        User user = userRepository.findAll().stream().filter(u->u.getName().equals(USER_NAME)).findFirst().get();
        userService.removeUser(user.getId());
        Assertions.assertFalse(userRepository.existsById(USER_ID));
    }

    @Test
    void checkUserExistenceTest(){
        saveUserForTest(USER_NAME);
        Assertions.assertTrue(userService.checkUserExistence(USER_NAME));
    }

    @Test
    void getUserByNameTest(){
        saveUserForTest(USER_NAME);
        Assertions.assertEquals(USER_NAME,userService.getUserByName(USER_NAME).getName());
        deleteUserForTest(USER_ID);
    }

    @Test
    void idAutoincrementSettingsTest(){
        List<String> names = new ArrayList<>(Arrays.asList("first","second","third"));
        List<Integer> ids = new ArrayList<>();
        names.forEach(this::saveUserForTest);
        userRepository.findAll().forEach(user->ids.add(user.getId()));
        Assertions.assertEquals(ids.get(2)-ids.get(1),ids.get(1)-ids.get(0));
        ids.forEach(this::deleteUserForTest);
    }

    void saveUserForTest(String name){
        User user = new User();
        user.setName(name);
        userRepository.saveAndFlush(user);
    }

    void deleteUserForTest(Integer id){
        userRepository.delete(userRepository.getReferenceById(id));
        userRepository.flush();
    }

}
