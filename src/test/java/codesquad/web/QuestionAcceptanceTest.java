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

    private static long AUTH_TEST = 1L;
    private static long LOGIN_TEST = 2L;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void form() {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void home() {
        ResponseEntity<String> response = template().getForEntity("/", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d", AUTH_TEST), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().contains(questionRepository.findById(AUTH_TEST).get().getContents()), is(true));
    }

    @Test
    public void create_no_login() {
        ResponseEntity<String> response = create(template());
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void create_login() {
        ResponseEntity<String> response = create(basicAuthTemplate());
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    private ResponseEntity<String> create(TestRestTemplate template) {
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();

        htmlFormDataBuilder.addParameter("title", "test");
        htmlFormDataBuilder.addParameter("contents", "test");

        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        return template.postForEntity("/questions", request, String.class);
    }

    @Test
    public void updateForm() {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity(String.format("/questions/%d/form", AUTH_TEST), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    @Test
    public void updateForm_no_login() {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d/form", AUTH_TEST), String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void delete_no_login() {
        ResponseEntity<String> response = delete(template());
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void delete_login() {
        ResponseEntity<String> response = delete(basicAuthTemplate(userRepository.findByUserId("sanjigi").get()));
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    private ResponseEntity<String> delete(TestRestTemplate template) {
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        htmlFormDataBuilder.addParameter("_method", "delete");

        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        return template.postForEntity(String.format("/questions/%d", LOGIN_TEST), request, String.class);
    }
}
