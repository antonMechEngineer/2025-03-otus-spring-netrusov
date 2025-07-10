package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.entities.AuthorEntity;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorEntityRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorEntityRepository authorEntityRepository;

    @Override
    public Flux<Author> findAll() {
        return authorEntityRepository.findAll().map(AuthorEntity::toDomainObject);
    }
}
