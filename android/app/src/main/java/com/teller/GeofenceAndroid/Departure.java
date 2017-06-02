package com.teller.GeofenceAndroid;

import java.util.Locale;

/**
 * Created by david on 12/17/16.
 */

public class Departure {

    private String requestId;
    private String siteId;
    private String destination;
    private String lineNumber;
    private double latitude;
    private double longitude;
    private double radius;

    public Departure() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String generateRequestId() {
        return String.format(Locale.ENGLISH, "%f|%f|%f", latitude, longitude, radius);
    }

    public Departure(double latitude, double longitude, double radius, String stationSiteId, String destination, String lineNumber, String requestId) {
        this.siteId = stationSiteId;
        this.destination = destination;
        this.lineNumber = lineNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.requestId = requestId;
    }

}
