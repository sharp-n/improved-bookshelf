package com.company.db.repositories;

import com.company.db.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    default List<User> getAllElements(){
        return new ArrayList<>(findAll());
    }


    default void addUser(String username){
        User user = new User();
        user.setName(username);
        save(user);
    }

    default User getUserByID(int id){
        return findById(id).get();
    }

    default void removeUser(int id){
        delete(getUserByID(id));
    }

    default boolean checkUserExistence(String name){
        ExampleMatcher userMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        User user = new User();
        user.setName(name);
        Example<User> userExample = Example.of(user,userMatcher);
        return exists(userExample);
    }

    default User getUserByName(String name) {
        List<User> users = new ArrayList<>();
        findAll().forEach(users::add);
        return users.stream().filter(user -> user.getName().equals(name)).findFirst().get();
    }

}
