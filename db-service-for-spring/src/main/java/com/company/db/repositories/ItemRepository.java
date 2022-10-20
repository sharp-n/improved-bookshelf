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



}
