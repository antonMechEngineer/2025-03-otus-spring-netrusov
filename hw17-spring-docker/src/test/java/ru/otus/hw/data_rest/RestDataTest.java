package ru.otus.hw.data_rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestDataTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "Check '{0}' endpoints.")
    @ValueSource(strings = {"authors", "books", "comments", "genres"})
    void testGetAuthors(String resourceName) throws Exception {
        this.mockMvc.perform(get("/repository/" +resourceName))
                .andExpect(status().isOk());
    }
}
