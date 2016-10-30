package com.mishima.tracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "user_timestamp", def = "{'userName' : 1, 'timestamp': -1}")
})
public class Location {

    @Id
    private String id;
    private String userName;
    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;
    private double altitudeAccuracy;
    private double heading;
    private double speed;
    private long timestamp;

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", accuracy=" + accuracy +
                ", altitude=" + altitude +
                ", altitudeAccuracy=" + altitudeAccuracy +
                ", heading=" + heading +
                ", speed=" + speed +
                ", timestamp=" + timestamp +
                '}';
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The unique id of the location", required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The user name for the location", required = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location latidude", required = true)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location longitude", required = true)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location accuracy", required = true)
    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location altitude", required = true)
    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location altitude accuracy", required = true)
    public double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location heading", required = true)
    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location speed", required = true)
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @JsonProperty(required = true)
    @ApiModelProperty(notes = "The location timestamp", required = true)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
