package ru.yandex.practicum.dto.shoppingCart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @PositiveOrZero
    private Integer newQuantity;
}
