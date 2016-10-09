package com.mishima.tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @since 1.0.RC5
 */
@Component
public class Groups {

    public String USER;
    public String ADMIN;

    @Autowired
    public Groups(Environment env) {
        USER = env.getProperty("stormpath.authorized.group.user");
        ADMIN = env.getProperty("stormpath.authorized.group.admin");
    }
}
