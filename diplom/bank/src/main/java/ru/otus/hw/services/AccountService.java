package ru.otus.hw.services;

import ru.otus.hw.models.Account;

import java.util.List;

public interface AccountService {

    Account findByUsername(String username);

    List<Account> findAll();

}
