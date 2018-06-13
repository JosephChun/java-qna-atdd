package codesquad.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import codesquad.domain.QuestionRepository;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void form() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}
