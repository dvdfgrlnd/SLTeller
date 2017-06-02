package com.teller.GeofenceAndroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 12/19/16.
 */

public class Result {
    private String siteId;

    private List<Departure> departureList = new ArrayList<>();
    private String response;

    public Result(String siteId) {
        this.siteId = siteId;
    }

    public void addDeparture(Departure dep) {
        departureList.add(dep);
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Departure> getDepartureList() {
        return departureList;
    }

    public void setDepartureList(List<Departure> departureList) {
        this.departureList = departureList;
    }

}
