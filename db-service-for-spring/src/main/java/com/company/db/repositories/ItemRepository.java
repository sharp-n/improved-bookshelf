package com.company.db.repositories;

import com.company.Book;
import com.company.Comics;
import com.company.db.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    default void addItem(com.company.Item itemCore, String userName, UserRepository userRepository){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                itemCore.getClass().getSimpleName(),
                itemCore.getTitle(),
                itemCore.getPages(),
                itemCore.isBorrowed(),
                userRepository.getUserByName(userName)
        );
        save(itemEntity);
    }

    default void addItem(Book book, String userName, UserRepository userRepository){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                book.getClass().getSimpleName(),
                book.getTitle(),
                book.getAuthor(),
                book.getDate(),
                book.getPages(),
                book.isBorrowed(),
                userRepository.getUserByName(userName)
        );
        save(itemEntity);
    }

    default void addItem(Comics comics, String userName, UserRepository userRepository){
        com.company.db.entities.Item item = new com.company.db.entities.Item(
                comics.getClass().getSimpleName(),
                comics.getTitle(),
                comics.getPublishing(),
                comics.getPages(),
                comics.isBorrowed(),
                userRepository.getUserByName(userName)
        );
        saveAndFlush(item);
    }

    default com.company.db.entities.Item getItemById(int id){
        return findById(id).get();
    }

    default void removeItem(int id){
        delete(getItemById(id));
    }

    default List<Item> getAllElements(){
        return new ArrayList<>(findAll());
    }

    default boolean checkItemExistence(int id){
        return existsById(id);
    }

    default boolean updateBorrowed(int id, boolean borrowedToSet){
        if (checkItemExistence(id)){
            com.company.db.entities.Item item = getItemById(id);
            if (item.isBorrowed()!=borrowedToSet){
                item.setBorrowed(borrowedToSet);
                save(item);
                return true;
            }
        }
        return false;
    }

}
