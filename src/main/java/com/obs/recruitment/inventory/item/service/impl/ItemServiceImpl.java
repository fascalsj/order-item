package com.obs.recruitment.inventory.item.service.impl;

import com.obs.recruitment.inventory.item.dto.request.ItemRequest;
import com.obs.recruitment.inventory.item.dto.response.ItemResponse;
import com.obs.recruitment.inventory.item.model.Item;
import com.obs.recruitment.inventory.item.repository.ItemRepository;
import com.obs.recruitment.inventory.item.repository.specification.ItemSpecification;
import com.obs.recruitment.inventory.item.service.ItemService;
import com.obs.recruitment.inventory.item.mapper.ItemMapper;
import com.obs.recruitment.inventory.shared.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemSpecification itemSpecification;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Item> itemOpt = itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            item.setDeleted(true);
            itemRepository.save(item);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }


    @Override
    public Page<ItemResponse> getAll(Pageable pageable, String search) {
        Page<Item> itemPage = itemRepository.findAll(itemSpecification.findAllPredicate(search), pageable);
        List<ItemResponse> itemResponses = itemPage.getContent().stream().map(itemMapper::mapToItemResponse)
                .toList();
        return new PageImpl<>(
                itemResponses,
                itemPage.getPageable(),
                itemPage.getTotalElements()
        );
    }

    @Override
    public void update(Integer id, ItemRequest itemRequest) {
        Optional<Item> itemOpt =itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            itemRequest.setId(id);
            Item item = itemOpt.get();
            Item itemMapped = itemMapper.mapToItem(item, itemRequest);
            itemRepository.save(itemMapped);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }

    @Override
    public ItemResponse get(Integer id) {
        Optional<Item> itemOpt = itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            return itemMapper.mapToItemResponse(item);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }

    @Override
    public void create(ItemRequest itemRequest) {
        Item itemMapped = itemMapper.mapToItem(itemRequest);
        itemRepository.save(itemMapped);
    }
}
