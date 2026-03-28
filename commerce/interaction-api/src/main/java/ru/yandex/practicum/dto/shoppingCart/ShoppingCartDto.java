package ru.yandex.practicum.dto.shoppingCart;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ShoppingCartDto {
    @NotNull
    private UUID cartId;
    @NotNull
    private Map<UUID, Integer> products;
}
