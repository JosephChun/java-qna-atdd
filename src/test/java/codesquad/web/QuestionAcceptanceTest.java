package codesquad.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import codesquad.domain.QuestionRepository;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

public class QuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void form() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void home() {
        ResponseEntity<String> response = template().getForEntity("/", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        assertThat(response.getBody().contains("dino title"), is(true));
        assertThat(response.getBody().contains("dino contents"), is(true));
    }

}
