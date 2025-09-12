package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.mapper.AccountMapper;
import ru.otus.hw.rest.dto.AccountDto;
import ru.otus.hw.services.AccountService;

@RequiredArgsConstructor
@RestController
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @GetMapping("/api/balance")
    public ResponseEntity<AccountDto> findUserBalance(Authentication authentication) {
        AccountDto accountDto = accountMapper.toDto(accountService.findByUsername(authentication.getName()));
        return ResponseEntity.ok(accountDto);
    }
}
