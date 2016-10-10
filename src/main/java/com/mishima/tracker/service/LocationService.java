package com.mishima.tracker.service;

import com.mishima.tracker.model.Location;
import com.stormpath.sdk.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @PreAuthorize("hasAuthority(@groups.USER)")
    public List<Location> getLocationsForAccount(Account account) {
        String userName = account.getUsername();
        log.debug("Returning locations for userName {}", userName);
        return mongoTemplate.find(query(where("userName").is(userName)).with(new Sort(DESC, "timestamp")), Location.class);
    }

    @PreAuthorize("hasAuthority(@groups.USER)")
    public Location findById(String id, Account account) {
        log.debug("Returning location with id {}", id);
        Location location = mongoTemplate.findById(id, Location.class);
        if( location == null ) {
            log.warn("Count not find location with id {}", id);
            return null;
        } else if( !location.getUserName().equals(account.getUsername())) {
            throw new SecurityException("Not authorized to view location");
        } else {
            log.debug("Found location with id {}", id);
            return location;
        }
    }

    @PreAuthorize("hasAuthority(@groups.USER)")
    public Location saveLocation(Location location, Account account) {
        log.debug("Saving location {}", location);
        location.setId(null); // Ensure new insert
        location.setUserName(account.getUsername());
        mongoTemplate.insert(location);
        return location;
    }

    @PreAuthorize("hasAuthority(@groups.USER)")
    public Location deleteLocation(String id, Account account) throws SecurityException {
        log.debug("Deleting location with id {}", id);
        Location location = mongoTemplate.findById(id, Location.class);
        if( location == null) {
            log.warn("Could not find location with id {}", id);
            return null;
        } else if( !location.getUserName().equals(account.getUsername())){
            throw new SecurityException("Not authorized to delete location");
        } else {
            mongoTemplate.remove(location);
            log.debug("Deleted location with id {}", id);
            return location;
        }
    }


}
