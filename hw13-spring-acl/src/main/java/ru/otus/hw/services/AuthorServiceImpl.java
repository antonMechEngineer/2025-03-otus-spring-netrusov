package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
