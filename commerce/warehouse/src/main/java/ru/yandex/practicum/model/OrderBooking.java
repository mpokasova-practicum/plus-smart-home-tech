package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name="bookings")
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id")
    private UUID bookingId;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @ElementCollection
    @CollectionTable(name = "booking_product", joinColumns = @JoinColumn(name = "booking_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}