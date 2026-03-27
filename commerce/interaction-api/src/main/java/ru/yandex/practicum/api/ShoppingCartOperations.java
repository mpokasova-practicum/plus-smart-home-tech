package ru.yandex.practicum.api;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingCart.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartOperations {
    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam(name = "username") @NotNull String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam(name = "username") @NotNull String username,
                                             @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivateShoppingCart(@RequestParam(name = "username") @NotNull String username);

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(@RequestParam(name = "username") @NotNull String username,
                                           @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam(name = "username") @NotNull String username,
                                   @RequestBody ChangeProductQuantityRequest request);
}
