package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "delivery.calculator")
@Data
public class DeliveryCostProperties {
    private BigDecimal baseRate;
    private BigDecimal warehouse1AddressMultiplier;
    private BigDecimal warehouse2AddressMultiplier;
    private BigDecimal fragileMultiplier;
    private BigDecimal weightMultiplier;
    private BigDecimal volumeMultiplier;
    private BigDecimal streetMultiplier;
}