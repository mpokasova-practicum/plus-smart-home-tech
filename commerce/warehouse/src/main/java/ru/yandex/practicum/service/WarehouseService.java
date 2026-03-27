package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {
    void createProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantity(ShoppingCartDto cartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();
}
