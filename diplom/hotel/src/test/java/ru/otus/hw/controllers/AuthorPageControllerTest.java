//package ru.otus.hw.controllers;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthorPageController.class)
//public class AuthorPageControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private AuthorService authorService;
//
//    @Test
//    void shouldReturnAllAuthorsPageWithAuthorsList() throws Exception {
//        when(authorService.findAll()).thenReturn(Collections.emptyList());
//        mvc.perform(get("/authors"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("allAuthors"));
//    }
//}
