package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.models.Account;
import ru.otus.hw.rest.dto.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toDto(Account account);

    Account fromDto(AccountDto accountDto);

}
