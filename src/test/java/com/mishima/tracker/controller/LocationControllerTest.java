package com.mishima.tracker.controller;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerTest {

    private static final Logger log = LoggerFactory.getLogger(LocationControllerTest.class);

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private Environment env;

    @Test
    public void locations() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(getAuthorization());
        ResponseEntity<List> response = template.exchange("/location/list", HttpMethod.GET, request, List.class);
        log.info("Response -> {}", response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    private HttpHeaders getAuthorization() {
        String plainCreds = env.getProperty("test.user.authorization");
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

}
