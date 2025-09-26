package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;

    @NotNull(message = "Balance cannot be null")
    @Digits(integer = 17, fraction = 2, message = "Balance must have maximum 17 integer digits and 2 fractional digits")
    private BigDecimal balance;

    @NotNull(message = "Username cannot be null")
    @Size(min = 1,max = 50, message = "Username cannot exceed 50 characters")
    private String username;
}
