package com.company;

import com.company.db.repositories.ItemRepository;
import com.company.db.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemRepositoryTests {

    private final String USER_NAME = "test user";

    Item testItem = new Journal(1,"test",45);
    private final String TITLE = "test";

    @BeforeEach
    void addUser(){
        if(!userRepository.checkUserExistence(USER_NAME)){
            userRepository.addUser(USER_NAME);
        }
    }

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("provideItemsToAdd")
    void addItemTest(Item providedItem){
        itemRepository.addItem(providedItem,USER_NAME,userRepository);
        List<com.company.db.entities.Item> items = new ArrayList<>();
        itemRepository.findAll().forEach(items::add);
        String provided = items.stream()
                .filter(item -> item.getUser().getName().equalsIgnoreCase(USER_NAME))
                .filter(item -> item.getTypeOfItem().equalsIgnoreCase(providedItem.getClass().getSimpleName()))
                .filter(item -> item.getTitle().equals(providedItem.getTitle()))
                .findFirst().get().getTitle();
        Assertions.assertEquals(providedItem.getTitle(),provided);
        deleteItemForTest(providedItem.getTitle());
    }

    private static Stream<Arguments> provideItemsToAdd(){
        return Stream.of(
                Arguments.of(new Book(1,"test book","test author",new Date(2002, Calendar.APRIL,2),456)),
                Arguments.of(new Comics(2,"test comics", 105,"test publishing")),
                Arguments.of(new Journal(3,"test journal",50))
        );
    }

    @Test
    void getItemByIdTest(){
        addTestItem(testItem,USER_NAME);
        Integer id = itemRepository.findAll().stream()
                .filter(item -> item.getUser().getName().equals(USER_NAME))
                .filter(item->item.getTitle().equals(TITLE))
                .findFirst().get().getId();
        Assertions.assertEquals(TITLE,itemRepository.getItemById(id).getTitle());
        deleteItemForTest(TITLE);
    }

    @Test
    void removeItemTest(){
        addTestItem(testItem,USER_NAME);
        Integer id=  getItemForTest(TITLE).getId();
        itemRepository.removeItem(id);
        Assertions.assertFalse(itemRepository.existsById(id));
    }

    @Test
    void getAllElementsTest(){
        addTestItem(testItem,USER_NAME);
        addTestItem(new Newspaper(1,"test newspaper",34),USER_NAME);
        List<com.company.db.entities.Item> items = itemRepository.getAllElements();
        Assertions.assertEquals(TITLE,getItemForTest(TITLE).getTitle());
        Assertions.assertEquals("test newspaper",getItemForTest("test newspaper").getTitle());
        deleteItemForTest(TITLE);
        deleteItemForTest("test newspaper");
    }

    @Test
    void checkItemExistenceTest(){
        addTestItem(testItem,USER_NAME);
        Assertions.assertTrue(itemRepository.checkItemExistence(getItemForTest(TITLE).getId()));
        deleteItemForTest(TITLE);
    }

    @Test
    void updateBorrowedTest(){
        addTestItem(testItem,USER_NAME);
        itemRepository.updateBorrowed(getItemForTest(TITLE).getId(),true);
        Assertions.assertTrue(getItemForTest(TITLE).isBorrowed());
        itemRepository.updateBorrowed(getItemForTest(TITLE).getId(),false);
        Assertions.assertFalse(getItemForTest(TITLE).isBorrowed());
        deleteItemForTest(TITLE);
    }

    void addTestItem(Item itemCore,String userName){
        com.company.db.entities.Item itemEntity = new com.company.db.entities.Item(
                itemCore.getClass().getSimpleName(),
                itemCore.getTitle(),
                itemCore.getPages(),
                itemCore.isBorrowed(),
                userRepository.getUserByName(userName)
        );
        itemRepository.save(itemEntity);
    }

    void deleteItemForTest(String title){
        itemRepository.delete(getItemForTest(title));
    }

    com.company.db.entities.Item getItemForTest(String title){
        return itemRepository.findAll().stream()
                .filter(item -> item.getUser().getName().equals(USER_NAME))
                .filter(item->item.getTitle().equals(title))
                .findFirst().get();
    }

}
