package com.mishima.tracker.model;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAltitudeAccuracy() {
        return altitudeAccuracy;
    }

    public void setAltitudeAccuracy(double altitudeAccuracy) {
        this.altitudeAccuracy = altitudeAccuracy;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
