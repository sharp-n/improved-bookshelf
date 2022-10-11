package com.company.db.services;

import com.company.db.entities.User;
import com.company.db.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    public final UserRepository userRepository;

    public List<User> getAllElements(){
        return new ArrayList<>(userRepository.findAll());
    }

    @Transactional
    public void addUser(String username){
        User user = new User();
        user.setName(username);
        userRepository.save(user);
    }

    public User getUserByID(int id){
        return userRepository.findById(id).get();
    }

    public void removeUser(int id){
        userRepository.delete(getUserByID(id));
    }

    public boolean checkUserExistence(String name){
        ExampleMatcher userMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        User user = new User();
        user.setName(name);
        Example<User> userExample = Example.of(user,userMatcher);
        return userRepository.exists(userExample);
    }

    public User getUserByName(String name) {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().filter(user -> user.getName().equals(name)).findFirst().get();
    }

}
