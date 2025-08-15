package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find all comments for book", key = "cbyb")
    public String findCommentsByBook(long bookId) {
        return commentService.findByBook(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find comment", key = "cbyid")
    public String findCommentById(long commentId) {
        return commentService.findById(commentId).map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(commentId));
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String payloadComment, long bookId) {
        var savedComment = commentService.insert(payloadComment, bookId);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String payloadComment, long bookId) {
        var savedComment = commentService.update(id, payloadComment, bookId);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}

