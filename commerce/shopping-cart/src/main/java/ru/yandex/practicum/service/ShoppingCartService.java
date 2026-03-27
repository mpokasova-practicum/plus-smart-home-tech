package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingCart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> products);

    void deactivateShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
