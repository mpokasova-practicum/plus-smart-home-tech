package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.payment.PaymentState;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;
    @Column(name = "total_payment")
    private Double totalPayment;
    @Column(name = "delivery_total")
    private Double deliveryTotal;
    @Column(name = "fee_total")
    private Double feeTotal;
    @Column(name = "product_total")
    private Double productTotal;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState paymentState = PaymentState.PENDING;
    @Column(name = "order_id")
    private UUID orderId;
}