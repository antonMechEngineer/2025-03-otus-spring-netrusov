package ru.otus.hw.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.rest.AuthorController;
import ru.otus.hw.rest.GlobalExceptionHandler;
import ru.otus.hw.rest.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorController.class)
@Import(GlobalExceptionHandler.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllAuthorsWithExistingAuthors() throws Exception {
        List<AuthorDto> expectedAuthors = Collections.singletonList(new AuthorDto(1L, "a"));
        when(authorService.findAll()).thenReturn(expectedAuthors.stream().map(AuthorDto::toDomainObject).toList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors").with(user("abc").password("abc")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").value("a"));
    }

    @Test
    void testGetAllAuthorsWhenNoAuthorsExist() throws Exception {
        when(authorService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/authors").with(user("abc").password("abc")))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}