package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mappers.PaymentMapper;
import ru.otus.hw.rest.dto.PaymentDto;
import ru.otus.hw.services.PaymentService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    private final PaymentMapper paymentMapper;

    @GetMapping("/api/payments")
    public ResponseEntity<List<PaymentDto>> findClientPayments(Authentication authentication) {
        var paymentDtos = paymentService.findByUsername(authentication.getName())
                .stream()
                .map(paymentMapper::toDto)
                .toList();
        return ResponseEntity.ok(paymentDtos);
    }

    @PutMapping("/api/payments/{id}")
    public ResponseEntity<PaymentDto> pay(@PathVariable("id") long id) {
        var finishedPayment = paymentService.pay(id);
        var finishedPaymentDto = paymentMapper.toDto(finishedPayment);
        return ResponseEntity.ok(finishedPaymentDto);
    }

    @PutMapping("/api/payments/{id}/cancel")
    public ResponseEntity<PaymentDto> cancel(@PathVariable("id") long id) {
        var finishedPayment = paymentService.cancel(id);
        var finishedPaymentDto = paymentMapper.toDto(finishedPayment);
        return ResponseEntity.ok(finishedPaymentDto);
    }
}
