package codesquad.web;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() throws Exception {
        QuestionDto newQuestion = createQuestionDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

    }

    private QuestionDto createQuestionDto() {
        return new QuestionDto("title Test", "contents Test");
    }
}
