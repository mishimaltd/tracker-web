package com.mishima.tracker.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {

    @Test
    public void testLocation() {
        Location location = new Location();
        String userName = "test.user@anon.com";
        location.setUserName(userName);
        assertEquals(userName, location.getUserName());
    }

}
