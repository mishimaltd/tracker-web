package com.mishima.tracker.service;

import com.mishima.tracker.model.Location;
import com.stormpath.sdk.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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

    /**
     * Returns all saved locations for the given account
     * @param from limit to locations saved since the given time in milliseconds utc
     * @param to limit to locations saved up until the given time in milliseconds utc
     * @param limit limit the total number of results
     * @param account The authorized account
     * @return matching Locations
     */
    public List<Location> findByAccount(long from, long to, int limit, Account account) {
        String userName = account.getUsername();
        log.debug("Returning locations for userName {}, from {}, to {}, limit {}", userName, from, to, limit);
        Query query = query(where("userName").is(userName)).with(new Sort(DESC, "timestamp"));
        if( from > 0 ) query.addCriteria(where("timestamp").gte(from));
        if( to > 0 ) query.addCriteria(where("timestamp").lt(to));
        if( limit > 0 ) query.limit(limit);
        return mongoTemplate.find(query, Location.class);
    }

    public Location findById(String id, Account account) throws SecurityException {
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

    public Location saveLocation(Location location, Account account) {
        log.debug("Saving location {}", location);
        location.setId(null); // Ensure new insert
        location.setUserName(account.getUsername());
        mongoTemplate.insert(location);
        return location;
    }

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
