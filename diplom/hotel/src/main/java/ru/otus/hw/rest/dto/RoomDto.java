package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Room;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class RoomDto {

    private long id;

    private Integer roomNumber;

    private Room.Type type;

    private BigDecimal pricePerDay;

    private List<LocalDate> occupiedDates;

}
