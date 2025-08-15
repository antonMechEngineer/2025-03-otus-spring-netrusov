package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GenrePageController.class, BookPageController.class, AuthorPageController.class})
@Import({SecurityConfiguration.class})
public class SecurityPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @ParameterizedTest(name = "{0} {1} return {2} status")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, int status) throws Exception {
        var request = buildRequest(method, url);
        mvc.perform(request)
                .andExpect(status().is(status))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldReturnExpectedStatusByRolePositive() throws Exception {
        mvc.perform(get("/insertBook").param("id", "1").with(user("def").password("abc").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnExpectedStatusByRoleNegative() throws Exception {
        mvc.perform(get("/insertBook").param("id", "1").with(user("def").password("abc")))
                .andExpect(status().isForbidden());
    }

    private MockHttpServletRequestBuilder buildRequest(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get);
        return methodMap.get(method).apply(url);

    }

    private static Stream<Arguments> getTestData() {
        return Stream.of(
                Arguments.of("get", "/authors", 302),
                Arguments.of("get", "/genres", 302),
                Arguments.of("get", "/books", 302),
                Arguments.of("get", "/insertBook", 302),
                Arguments.of("get", "/browseBook", 302),
                Arguments.of("get", "/deleteBook", 302),
                Arguments.of("get", "/editBook", 302)
        );
    }

}
