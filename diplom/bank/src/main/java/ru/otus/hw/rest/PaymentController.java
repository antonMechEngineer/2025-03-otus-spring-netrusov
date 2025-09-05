package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mapper.PaymentMapper;
import ru.otus.hw.models.Payment;
import ru.otus.hw.rest.dto.PaymentDto;
import ru.otus.hw.services.PaymentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    private final PaymentMapper paymentMapper;

    @GetMapping("/api/payments")
    public ResponseEntity<List<PaymentDto>> findAll(Authentication authentication) {
        List<PaymentDto> paymentDtos = paymentService.findByUsername(authentication.getName())
                .stream()
                .map(paymentMapper::toDto)
                .toList();
        return ResponseEntity.ok(paymentDtos);
    }

    @PutMapping("/api/payments/{id}")
    public ResponseEntity<PaymentDto> pay(Authentication authentication, @PathVariable Long id) {
        Payment finishedPayment = paymentService.pay(authentication.getName(), id);
        PaymentDto finishedPaymentDto = paymentMapper.toDto(finishedPayment);
        return ResponseEntity.ok(finishedPaymentDto);
    }

    @PutMapping("/api/payments/{id}/cancel")
    public ResponseEntity<PaymentDto> cancel(Authentication authentication, @PathVariable Long id) {
        Payment finishedPayment = paymentService.cancel(authentication.getName(), id);
        PaymentDto finishedPaymentDto = paymentMapper.toDto(finishedPayment);
        return ResponseEntity.ok(finishedPaymentDto);
    }
}
