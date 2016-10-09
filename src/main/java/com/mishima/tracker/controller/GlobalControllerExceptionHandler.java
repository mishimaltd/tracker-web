package com.mishima.tracker.controller;

import com.mishima.tracker.exception.NotLoggedInException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // 401
    @ExceptionHandler(NotLoggedInException.class)
    @ResponseBody
    public Map<String,Object> handleUserNotLoggedIn() {
        Map<String,Object> model = newHashMap();
        model.put("timestamp", new DateTime(DateTimeZone.UTC).getMillis());
        model.put("status", 401);
        model.put("message", "Not logged in");
        model.put("exception", NotLoggedInException.class.getCanonicalName());
        return model;
    }




}
