package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude
    @Column(name = "room_number")
    private Integer roomNumber;

    @EqualsAndHashCode.Exclude
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @EqualsAndHashCode.Exclude
    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders;

    public enum Type {
        STANDARD,
        PREMIUM,
        LUX
    }

}
