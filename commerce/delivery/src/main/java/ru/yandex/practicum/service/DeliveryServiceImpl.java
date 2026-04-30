package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.OrderOperations;
import ru.yandex.practicum.api.WarehouseOperations;
import ru.yandex.practicum.config.DeliveryCostProperties;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.delivery.DeliveryState;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final WarehouseOperations warehouseClient;
    private final OrderOperations orderClient;
    private final DeliveryCostProperties costProperties;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        log.info("Создаем новую доставку в БД: deliveryDto={}", deliveryDto);
        Delivery delivery = deliveryMapper.toDelivery(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        log.info("Возвращаем доставку с присвоенным идентификатором: {}", delivery);
        return deliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        log.info("Рассчитываем стоимость доставки");
        Delivery delivery = deliveryRepository.findById(orderDto.getDeliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Такой доставки не найдено: deliveryId = " + orderDto.getDeliveryId()));
        Address warehouseAddress = delivery.getFromAddress();
        Address destinationAddress = delivery.getToAddress();
        BigDecimal totalCost = costProperties.getBaseRate();
        totalCost = warehouseAddress.getCity().equals("ADDRESS_1") ?
                totalCost.add(totalCost.multiply(costProperties.getWarehouse1AddressMultiplier())) :
                totalCost.add(totalCost.multiply(costProperties.getWarehouse2AddressMultiplier()));
        totalCost = orderDto.getFragile() == true ? totalCost.add(totalCost.multiply(costProperties.getFragileMultiplier())) : totalCost;
        totalCost = totalCost.add(BigDecimal.valueOf(orderDto.getDeliveryWeight()).multiply(costProperties.getWeightMultiplier()));
        totalCost = totalCost.add(BigDecimal.valueOf(orderDto.getDeliveryVolume()).multiply(costProperties.getVolumeMultiplier()));
        totalCost = warehouseAddress.getStreet().equals(destinationAddress.getStreet()) ?
                totalCost : totalCost.add(totalCost.multiply(costProperties.getStreetMultiplier()));
        log.info("Возвращаем стоимость доставки: {}", totalCost);
        return totalCost;
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        log.info("Передаем товар в доставку: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        delivery = deliveryRepository.save(delivery);
        orderClient.assembly(orderId);
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(orderId, delivery.getDeliveryId()));
        log.info("Товар передан в доставку: orderId={}", orderId);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        log.info("Проставить признак успешной доставки товара: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.delivery(orderId);
        log.info("Успешная доставка товара: orderId={}", orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        log.info("Проставить признак неуспешной доставки товара: orderId={}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException
                        ("Доставки для такого заказа не найдено: orderId = " + orderId));
        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.deliveryFailed(orderId);
        log.info("Успешная доставка товара: orderId={}", orderId);
    }
}