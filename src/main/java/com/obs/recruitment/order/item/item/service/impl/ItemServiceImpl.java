package com.obs.recruitment.order.item.item.service.impl;

import com.obs.recruitment.order.item.item.dto.request.ItemRequest;
import com.obs.recruitment.order.item.item.dto.response.ItemResponse;
import com.obs.recruitment.order.item.item.mapper.ItemMapper;
import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.repository.ItemRepository;
import com.obs.recruitment.order.item.item.repository.specification.ItemSpecification;
import com.obs.recruitment.order.item.item.service.ItemService;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
            throw new DataNotFoundException("Item not found " + id);
        }
    }


    @Override
    public Page<ItemResponse> getAll(Pageable pageable, String search) {
        Specification<Item> allPredicate = itemSpecification.findAllPredicate(search);
        Page<Item> itemPage = itemRepository.findAll(allPredicate, pageable);
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
        Optional<Item> itemOpt = itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            itemRequest.setId(id);
            Item item = itemOpt.get();
            Item itemMapped = itemMapper.mapToItem(item, itemRequest);
            upsert(itemMapped);
        } else {
            throw new DataNotFoundException("Item not found " + id);
        }
    }

    @Override
    public ItemResponse getResponse(Integer id) {
        Item item = get(id);
        return itemMapper.mapToItemResponse(item);
    }

    @Override
    public Item get(Integer id) {
        return itemRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Item not found " + id));
    }

    @Override
    public void create(ItemRequest itemRequest) {
        Item itemMapped = itemMapper.mapToItem(itemRequest);
        upsert(itemMapped);
    }

    @Override
    public void upsert(Item item) {
        itemRepository.save(item);
    }
}
