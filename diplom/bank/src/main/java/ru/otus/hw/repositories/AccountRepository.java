package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
