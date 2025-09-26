package ru.otus.hw.services;

import ru.otus.hw.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String ERROR_USER_NOT_FOUND = "User name = %s not found and account respectively!";

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public Account findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new EntityNotFoundException(format(ERROR_USER_NOT_FOUND, username))).getAccount();
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
