package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private Long id;

    @NotNull(message = "Room number cannot be null")
    @Positive(message = "Room number must be positive")
    private Integer roomNumber;

    @NotNull(message = "Room type cannot be null")
    private Room.Type type;

    @NotNull(message = "Price per day cannot be null")
    @Positive(message = "Price per day must be positive")
    @Digits(integer = 17, fraction = 2, message = "Price per day must have maximum 17 integer digits and 2 fractional digits")
    private BigDecimal pricePerDay;

    private List<LocalDate> occupiedDates;

}
