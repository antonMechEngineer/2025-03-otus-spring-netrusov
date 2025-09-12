package ru.otus.hw.services;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public Account findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new).getAccount();
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
