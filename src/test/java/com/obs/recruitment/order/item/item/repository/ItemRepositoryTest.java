package com.obs.recruitment.order.item.item.repository;

import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.repository.specification.ItemSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    void findByIdAndIsDeleted() {
        Item item = new Item();
        item.setDeleted(true);
        item.setId(1);
        item.setName("Botol");
        item.setStock(10);
        item.setPrice(BigDecimal.valueOf(5000));
        item.setDeleted(true);
        itemRepository.save(item);

        Optional<Item> itemOpt = itemRepository.findByIdAndIsDeleted(1, true);
        Assertions.assertNotNull(itemOpt);
    }

    @Test
    void findAll() {
        Item item = new Item();
        item.setDeleted(true);
        item.setId(1);
        item.setName("Botol");
        item.setStock(10);
        item.setPrice(BigDecimal.valueOf(5000));
        item.setDeleted(true);
        itemRepository.save(item);

        ItemSpecification itemSpecification = new ItemSpecification();
        Specification<Item> search = itemSpecification.findAllPredicate("bot");

        List<Item> items = itemRepository.findAll(search);
        Assertions.assertNotNull(items);
    }
}