package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class CascadeDeleteEventsListenerGenre extends AbstractMongoEventListener<Genre> {

    private final GenreRepository genreRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Genre> event) {
        super.onAfterDelete(event);
        var source = event.getSource();
        var id = source.get("_id").toString();
        genreRepository.deleteById(Long.valueOf(id));
    }
}
