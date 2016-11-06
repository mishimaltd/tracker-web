package com.mishima.tracker.controller;

import com.mishima.tracker.exception.NotLoggedInException;
import com.mishima.tracker.model.Location;
import com.mishima.tracker.service.LocationService;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ApiOperation(value = "getLocations", nickname = "getLocations", notes="Returns location information for the logged in user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "From time", dataType = "long", paramType = "query", defaultValue="0"),
            @ApiImplicitParam(name = "to", value = "Until time", dataType = "long", paramType = "query", defaultValue="0"),
            @ApiImplicitParam(name = "limit", value = "Limit results", dataType = "long", paramType = "query", defaultValue="0")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}
    )
    @RequestMapping(value="/locations/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Location>> getLocations(HttpServletRequest req,
                                                       @RequestParam(value = "from", required = false, defaultValue = "0") long from,
                                                       @RequestParam(value = "to", required = false, defaultValue = "0") long to,
                                                       @RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
        return new ResponseEntity<List<Location>>(locationService.findByAccount(from, to, limit, getAccount(req)), HttpStatus.OK);
    }


    @ApiOperation(value = "getLocation", nickname = "getLocation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Location id", required = true, dataType = "string", paramType = "path")
    })
    @RequestMapping(value="/locations/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Location.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}
    )
    @ResponseBody
    public ResponseEntity<Location> findLocationById(HttpServletRequest req, @PathVariable("id") String id) {
        try {
            Location location = locationService.findById(id, getAccount(req));
            if( location == null ) {
                return new ResponseEntity<Location>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Location>(location, HttpStatus.OK);
            }
        } catch( SecurityException ex ) {
            return new ResponseEntity<Location>(HttpStatus.UNAUTHORIZED);
        }
    }


    @ApiOperation(value = "saveLocation", nickname = "saveLocation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "Location", required = true, dataType = "Location", paramType = "body")
    })
    @RequestMapping(value="/locations/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Location.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}
    )
    @ResponseBody
    public ResponseEntity<Location> saveLocation(HttpServletRequest req, @RequestBody Location location) {
        return new ResponseEntity<Location>(locationService.saveLocation(location, getAccount(req)), HttpStatus.CREATED);
    }

    @ApiOperation(value = "deleteLocation", nickname = "deleteLocation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Location id", required = true, dataType = "string", paramType = "path")
    })
    @RequestMapping(value="/locations/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}
    )
    @ResponseBody
    public ResponseEntity<String> deleteLocation(HttpServletRequest req, @PathVariable("id") String id) {
        try {
            Location deleted = locationService.deleteLocation(id, getAccount(req));
            if( deleted != null ) {
                return new ResponseEntity<String>(HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            }
        } catch( SecurityException ex ) {
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
    }

    private Account getAccount(HttpServletRequest req) {
        Account account = AccountResolver.INSTANCE.getAccount(req);
        if( account == null ) {
            throw new NotLoggedInException("Not logged in");
        }
        return account;
    }

}
