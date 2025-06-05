package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class CascadeDeleteEventsListenerAuthor extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Author> event) {
        super.onAfterDelete(event);
        var source = event.getSource();
        var id = source.get("_id").toString();
        bookRepository.deleteById(Long.valueOf(id));
    }
}
