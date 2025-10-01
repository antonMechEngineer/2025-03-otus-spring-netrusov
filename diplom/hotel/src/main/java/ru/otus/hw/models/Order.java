package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @Column(name = "begin_rent")
    private LocalDate beginRent;

    @EqualsAndHashCode.Exclude
    @Column(name = "end_rent")
    private LocalDate endRent;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @EqualsAndHashCode.Exclude
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @EqualsAndHashCode.Exclude
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Status {
        NOT_PAID,
        PAID,
        CANCEL,
        PAYMENT_REQUEST,
        AUTO_CANCEL
    }

    public Order(LocalDate beginRent, LocalDate endRent, User user, Room room) {
        this.beginRent = beginRent;
        this.endRent = endRent;
        this.user = user;
        this.room = room;
        this.status = Status.NOT_PAID;
        long daysBetween = ChronoUnit.DAYS.between(beginRent, endRent);
        this.totalPrice = room.getPricePerDay().multiply(BigDecimal.valueOf(daysBetween));
        this.createdAt = LocalDateTime.now();
    }
}
