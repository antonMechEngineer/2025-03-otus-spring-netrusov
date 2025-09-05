package ru.otus.hw.services;

import ru.otus.hw.models.User;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User editInfo(User user);

    User findCurrentUser();
}
