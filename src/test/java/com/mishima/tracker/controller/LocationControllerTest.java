package com.mishima.tracker.controller;

import com.mishima.tracker.model.Location;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerTest {

    private static final Logger log = LoggerFactory.getLogger(LocationControllerTest.class);

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private Environment env;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Location otherUserLocation;

    @Before
    public void setup() throws Exception {
        otherUserLocation = location();
        otherUserLocation.setUserName("other");
        mongoTemplate.insert(otherUserLocation);
    }

    @After
    public void teardown() throws Exception {
        mongoTemplate.remove(mongoTemplate.findById(otherUserLocation.getId(), Location.class));
    }


    @Test
    public void locations() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(getAuthorization());
        ResponseEntity<List> response = template.exchange("/location/list", HttpMethod.GET, request, List.class);
        log.info("Response -> {}", response.getBody());
        assertThat(response.getBody().isEmpty(), is( false));
    }

    @Test
    public void save() throws Exception {
        Location location = location();
        HttpEntity<Location> request = new HttpEntity<Location>(location, getAuthorization());
        ResponseEntity<Location> response = template.exchange("/location/save", HttpMethod.POST, request, Location.class);
        log.info("Response -> {}", response.getBody());
        assertThat(response.getStatusCode(), is( HttpStatus.CREATED));
    }

    @Test
    public void findById() throws Exception {
        Location location = saveLocation();
        ResponseEntity<Location> response = findById(location.getId());
        log.info("Response -> {}", response);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(location.getId()));
    }

    @Test
    public void findByUnauthorizedId() throws Exception {
        ResponseEntity<Location> response = findById(otherUserLocation.getId());
        log.info("Response -> {}", response);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void findByMissingId() throws Exception {
        ResponseEntity<Location> findResponse = findById("missing");
        log.info("Response -> {}", findResponse);
        assertThat(findResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void delete() throws Exception {
        Location location = saveLocation();
        HttpEntity<String> deleteRequest = new HttpEntity<String>(getAuthorization());
        ResponseEntity<String> deleteResponse = template.exchange("/location/delete/{id}", HttpMethod.DELETE, deleteRequest, String.class, location.getId());
        log.info("Response -> {}", deleteResponse);
        assertThat(deleteResponse.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<Location> findResponse = findById(location.getId());
        assertThat(findResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteMissing() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(getAuthorization());
        ResponseEntity<String> response = template.exchange("/location/delete/{id}", HttpMethod.DELETE, request, String.class, "missing");
        log.info("Response -> {}", response);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void deleteUnauthorized() throws Exception {
        HttpEntity<String> request = new HttpEntity<String>(getAuthorization());
        ResponseEntity<String> response = template.exchange("/location/delete/{id}", HttpMethod.DELETE, request, String.class, otherUserLocation.getId());
        log.info("Response -> {}", response);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    private HttpHeaders getAuthorization() {
        String plainCreds = env.getProperty("TEST_USER_AUTH");
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    private Location location() {
        Location location = new Location();
        location.setAccuracy(1d);
        location.setAltitude(1d);
        location.setAltitudeAccuracy(1d);
        location.setHeading(1d);
        location.setLatitude(1d);
        location.setLongitude(1d);
        location.setSpeed(1d);
        location.setTimestamp(System.currentTimeMillis());
        return location;
    }

    private Location saveLocation() {
        Location location = location();
        HttpEntity<Location> request = new HttpEntity<Location>(location, getAuthorization());
        ResponseEntity<Location> response = template.exchange("/location/save", HttpMethod.POST, request, Location.class);
        assertThat(response.getStatusCode(), is( HttpStatus.CREATED));
        return response.getBody();
    }

    private ResponseEntity<Location> findById(String id) {
        HttpEntity<String> findRequest = new HttpEntity<String>(getAuthorization());
        return template.exchange("/location/get/{id}", HttpMethod.GET, findRequest, Location.class, id);
    }

}
