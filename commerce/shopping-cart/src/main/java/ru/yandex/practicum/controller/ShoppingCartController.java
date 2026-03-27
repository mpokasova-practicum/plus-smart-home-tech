package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.dto.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.api.ShoppingCartOperations;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController implements ShoppingCartOperations {
    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> products) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @Override
    public void deactivateShoppingCart(String username) {
        shoppingCartService.deactivateShoppingCart(username);
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products) {
        return shoppingCartService.removeFromShoppingCart(username, products);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        return shoppingCartService.changeQuantity(username, request);
    }
}
