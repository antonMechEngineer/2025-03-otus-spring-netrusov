//package ru.otus.hw.rest.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import ru.otus.hw.rest.GlobalExceptionHandler;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ContextConfiguration(classes = GenreController.class)
//@WebMvcTest(controllers = GenreController.class)
//@Import(GlobalExceptionHandler.class)
//class GenreControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GenreService genreService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetAllGenresWhenGenresExist() throws Exception {
//        List<GenreDto> expectedGenres = Arrays.asList(new GenreDto(1L, "a"), new GenreDto(2L, "b"));
//        given(genreService.findAll()).willReturn(Arrays.asList(expectedGenres.get(0).toDomainObject(), expectedGenres.get(1).toDomainObject()));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("a"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2L))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("b"));
//    }
//
//    @Test
//    void testGetAllGenresWhenNoGenresExist() throws Exception {
//        given(genreService.findAll()).willReturn(Collections.emptyList());
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/genres"))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//    }
//}