package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.hw.entities.GenreEntity;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreEntityRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreEntityRepository genreEntityRepository;

    @Override
    public Flux<Genre> findAll() {
        return genreEntityRepository.findAll().map(GenreEntity::toDomainObject);
    }
}
