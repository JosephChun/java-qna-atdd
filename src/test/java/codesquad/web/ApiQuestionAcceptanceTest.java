package codesquad.web;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

    private static final long QUESTION_TEST = 1L;
    private static final long QUESTION_NO_LOGIN = 2L;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void create_login() throws Exception {
        QuestionDto newQuestion = createQuestionDto(QUESTION_TEST);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        String location = response.getHeaders().getLocation().getPath();
        log.debug("location {}", location);
        QuestionDto dbQuestion = basicAuthTemplate().getForObject(location, QuestionDto.class);
        assertThat(dbQuestion, is(newQuestion));
    }

    @Test
    public void create_no_login() throws Exception {
        QuestionDto newQuestion = createQuestionDto(QUESTION_NO_LOGIN);
        ResponseEntity<String> response = template().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private QuestionDto createQuestionDto(long id) {
        return new QuestionDto(id,"title Test", "contents Test");
    }




}
