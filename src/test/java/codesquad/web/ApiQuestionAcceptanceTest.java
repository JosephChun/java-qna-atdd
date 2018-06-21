package codesquad.web;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    private static final long EXIST_ID = 1L;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void create_login() throws Exception {
        QuestionDto newQuestion = createQuestionDto(EXIST_ID);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        log.debug("location {}", location);
        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion, is(newQuestion));
    }

    @Test
    public void create_no_login() throws Exception {
        QuestionDto newQuestion = createQuestionDto(EXIST_ID);
        ResponseEntity<String> response = template().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void update_login() {
        QuestionDto newQuestion = createQuestionDto(EXIST_ID);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto updatedQuestion = new QuestionDto(EXIST_ID, "title Test", "contents Test");
        basicAuthTemplate().put(location, updatedQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(updatedQuestion, is(dbQuestion));
    }

    @Test
    public void update_no_login() {
        QuestionDto newQuestion = createQuestionDto(EXIST_ID);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto updatedQuestion = new QuestionDto(EXIST_ID, "title Test2", "contents Test2");
        template().put(location, updatedQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(updatedQuestion, not(dbQuestion));
    }

    @Test
    public void delete_login() {
        QuestionDto newQuestion = createQuestionDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        log.debug("location : {}", location);
        basicAuthTemplate().delete(location);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertNull(dbQuestion);
    }

    @Test
    public void delete_no_login() {
        QuestionDto newQuestion = createQuestionDto(EXIST_ID);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        QuestionDto deletedQuestion = new QuestionDto(EXIST_ID, "title Test4", "contents Test4");
        template().delete(location, deletedQuestion);

        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion, is(newQuestion));
    }

    private QuestionDto createQuestionDto() {
        return new QuestionDto("title Test", "contents Test");
    }

    private QuestionDto createQuestionDto(long id) {
        return new QuestionDto(id,"title Test", "contents Test");
    }
}
