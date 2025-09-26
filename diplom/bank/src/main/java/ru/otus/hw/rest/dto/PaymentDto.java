package ru.otus.hw.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Payment;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private Long id;

    @NotBlank(message = "Buy type cannot be blank")
    @Size(min = 1, max = 255, message = "Password must be between 1 and 255 characters")
    private String buy;

    @NotNull(message = "Buy ID cannot be null")
    @DecimalMin(value = "1", message = "Buy ID must be greater than 0")
    private long buyId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 50, message = "Password must be between 1 and 50 characters")
    private String username;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Price must have maximum 17 integer digits and 2 fractional digits")
    private BigDecimal price;

    @NotNull(message = "Status cannot be null")
    @Size(min = 1, max = 20, message = "Password must be between 1 and 20 characters")
    private Payment.Status status;

}
