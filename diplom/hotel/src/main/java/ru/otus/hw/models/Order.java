package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    //todo: проставить валидацию состояний
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "begin_rent")
    private LocalDate beginRent;

    @Column(name = "end_rent")
    private LocalDate endRent;

    @ManyToOne
    private User user;

    @OneToOne
    private Room room;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Status {NOT_PAID, PAID, CANCEL, PAYMENT_REQUEST, AUTO_CANCEL}

    public Order(LocalDate beginRent,
                 LocalDate endRent,
                 User user,
                 Room room){
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
