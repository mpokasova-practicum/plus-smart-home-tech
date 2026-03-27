package ru.yandex.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.api.WarehouseOperations;
import ru.yandex.practicum.dto.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.DeactivateCartException;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.CartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private WarehouseOperations warehouseOperations;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateShoppingCart(username);
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> products) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateShoppingCart(username);
        checkCartIsActive(cart);
        Map<UUID, Integer> oldProducts = cart.getProducts();
        oldProducts.putAll(products);
        cart.setProducts(oldProducts);

        BookedProductsDto bookedProductsDto = warehouseOperations.checkProductQuantity(cartMapper.toCartDto(cart));
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public void deactivateShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateShoppingCart(username);
        checkCartIsActive(cart);
        cart.setActive(false);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateShoppingCart(username);
        checkCartIsActive(cart);
        Map<UUID, Integer> oldProducts = cart.getProducts();
        for (UUID removeId : products) {
            if (oldProducts.containsKey(removeId)) {
                oldProducts.remove(removeId);
            } else {
                throw new NoProductsInShoppingCartException("Продукта с данным id нет в корзине");
            }
        }
        cart.setProducts(oldProducts);
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);
        ShoppingCart cart = getOrCreateShoppingCart(username);
        checkCartIsActive(cart);
        Map<UUID, Integer> oldProducts = cart.getProducts();
        if (oldProducts.containsKey(request.getProductId())) {
            oldProducts.put(request.getProductId(), request.getNewQuantity());
        } else {
            throw new NoProductsInShoppingCartException("Продукта с данным id нет в корзине");
        }
        cart.setProducts(oldProducts);

        BookedProductsDto bookedProductsDto = warehouseOperations.checkProductQuantity(cartMapper.toCartDto(cart));

        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    private void validateUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException(username);
        }
    }

    private ShoppingCart getOrCreateShoppingCart(String username) {
        return cartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUsername(username);
                    return cartRepository.save(newCart);
                });
    }

    private void checkCartIsActive(ShoppingCart cart) {
        if(!cart.getActive()) {
            throw new DeactivateCartException("Корзина пользователя " + cart.getUsername() + " не активна");
        }
    }
}
