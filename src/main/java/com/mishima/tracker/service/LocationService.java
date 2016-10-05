package com.mishima.tracker.service;

import com.mishima.tracker.model.Location;
import com.stormpath.sdk.account.Account;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    @PreAuthorize("hasAuthority(@groups.USER)")
    public List<Location> getLocationsForAccount(Account account) {
        return mongoTemplate.find(query(where("userName").is(account.getUsername())).with(new Sort(DESC, "timestamp")), Location.class);
    }

    @PreAuthorize("hasAuthority(@groups.USER)")
    public Location saveLocation(Location location, Account account) {
        location.setId(null); // Ensure new insert
        location.setUserName(account.getUsername());
        mongoTemplate.insert(location);
        return location;
    }

}
