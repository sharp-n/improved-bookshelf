package com.company.db.services;

import com.company.Book;
import com.company.Comics;
import com.company.db.entities.Item;
import com.company.db.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemService {

    ItemRepository itemRepository;

    public void addItem(com.company.Item itemCore, String userName, UserService userService){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                itemCore.getClass().getSimpleName(),
                itemCore.getTitle(),
                itemCore.getPages(),
                itemCore.isBorrowed(),
                userService.getUserByName(userName)
        );
        itemRepository.save(itemEntity);
    }

    public void addBook(Book book, String userName, UserService userService){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                book.getClass().getSimpleName(),
                book.getTitle(),
                book.getAuthor(),
                book.getDate(),
                book.getPages(),
                book.isBorrowed(),
                userService.getUserByName(userName)
        );
        itemRepository.save(itemEntity);
    }

    public void addComics(Comics comics, String userName, UserService userService){
        com.company.db.entities.Item item = new com.company.db.entities.Item(
                comics.getClass().getSimpleName(),
                comics.getTitle(),
                comics.getPublishing(),
                comics.getPages(),
                comics.isBorrowed(),
                userService.getUserByName(userName)
        );
        itemRepository.saveAndFlush(item);
    }

    public com.company.db.entities.Item getItemById(int id) throws NoSuchElementException {
            return itemRepository.findById(id).get();
    }

    public void removeItem(int id){
        try {
            itemRepository.delete(getItemById(id));
        } catch (NoSuchElementException noSuchElementException){

        }
    }

    public List<Item> getAllElements(){
        return new ArrayList<>(itemRepository.findAll());
    }

    public boolean checkItemExistence(int id){
        return itemRepository.existsById(id);
    }

    public boolean updateBorrowed(int id, boolean borrowedToSet) throws NoSuchElementException{
        if (checkItemExistence(id)){
            com.company.db.entities.Item item = getItemById(id);
            if (item.isBorrowed()!=borrowedToSet){
                item.setBorrowed(borrowedToSet);
                itemRepository.save(item);
                return true;
            }
        }
        return false;
    }



}
