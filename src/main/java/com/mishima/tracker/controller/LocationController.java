package com.mishima.tracker.controller;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.mishima.tracker.model.Location;
import com.mishima.tracker.service.LocationService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(value="/location/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Location>  getLocations(HttpServletRequest req) {
        Account account = AccountResolver.INSTANCE.getAccount(req);
        return locationService.getLocationsForAccount(account);
    }

    @RequestMapping(value="/location/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Location saveLocation(HttpServletRequest req, @RequestBody Location location) {
        Account account = AccountResolver.INSTANCE.getAccount(req);
        return locationService.saveLocation(location, account);
    }

}
