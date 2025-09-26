package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.models.Account;
import ru.otus.hw.rest.dto.AccountDto;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user.username", target = "username")
    AccountDto toDto(Account account);

    Account fromDto(AccountDto accountDto);

}
