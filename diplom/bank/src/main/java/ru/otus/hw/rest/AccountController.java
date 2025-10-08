package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mappers.AccountMapper;
import ru.otus.hw.rest.dto.AccountDto;
import ru.otus.hw.services.AccountService;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @GetMapping("/api/balance")
    public ResponseEntity<AccountDto> findClientBalance(Authentication authentication) {
        var account = accountService.findByUsername(authentication.getName());
        var accountDto = accountMapper.toDto(account);
        return ResponseEntity.ok(accountDto);
    }
}
