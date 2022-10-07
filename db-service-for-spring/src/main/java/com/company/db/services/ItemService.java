package com.company.db.services;

import com.company.Book;
import com.company.Comics;
import com.company.Item;
import com.company.db.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    public void addItem(Item itemCore, String userName){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                itemCore.getClass().getSimpleName(),
                itemCore.getTitle(),
                itemCore.getPages(),
                itemCore.isBorrowed(),
                userService.getUserByName(userName)
        );
        itemRepository.save(itemEntity);
    }

    public void addItem(Book book, String userName){
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

    public void addItem(Comics comics, String userName){
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

    public com.company.db.entities.Item getItemById(int id){
        return itemRepository.findById(id).get();
    }

    public void removeItem(int id){
        itemRepository.delete(getItemById(id));
    }

    public List<com.company.db.entities.Item> getAllElements(){
        return new ArrayList<>(itemRepository.findAll());
    }

    public boolean checkItemExistence(int id){
        return itemRepository.existsById(id);
    }

    public boolean updateBorrowed(int id, boolean borrowedToSet){
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