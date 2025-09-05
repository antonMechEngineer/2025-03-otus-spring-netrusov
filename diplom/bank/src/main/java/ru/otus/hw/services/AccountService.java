package ru.otus.hw.services;

import ru.otus.hw.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account findByUsername(String username);

    List<Account> findAll();

}
