package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "payments")
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @Column(name = "buy")
    private String buy;

    @EqualsAndHashCode.Exclude
    @Column(name = "buy_id")
    private long buyId;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @EqualsAndHashCode.Exclude
    @Column(name = "price")
    private BigDecimal price;

    @EqualsAndHashCode.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.NOT_PAID;

    @EqualsAndHashCode.Exclude
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Status {
        NOT_PAID,
        PAID,
        CANCEL,
        UNSUCCESSFUL
    }
}
