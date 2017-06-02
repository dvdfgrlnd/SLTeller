
package com.teller.TransportInfo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainGroup {

    @SerializedName("Departures")
    @Expose
    private List<Departure_> departures = new ArrayList<Departure_>();
    @SerializedName("StopPointDeviations")
    @Expose
    private List<Object> stopPointDeviations = new ArrayList<Object>();
    @SerializedName("CurrentServerTime")
    @Expose
    private String currentServerTime;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("JourneyProduct")
    @Expose
    private Integer journeyProduct;
    @SerializedName("TransportSymbol")
    @Expose
    private String transportSymbol;

    /**
     * 
     * @return
     *     The departures
     */
    public List<Departure_> getDepartures() {
        return departures;
    }

    /**
     * 
     * @param departures
     *     The Departures
     */
    public void setDepartures(List<Departure_> departures) {
        this.departures = departures;
    }

    /**
     * 
     * @return
     *     The stopPointDeviations
     */
    public List<Object> getStopPointDeviations() {
        return stopPointDeviations;
    }

    /**
     * 
     * @param stopPointDeviations
     *     The StopPointDeviations
     */
    public void setStopPointDeviations(List<Object> stopPointDeviations) {
        this.stopPointDeviations = stopPointDeviations;
    }

    /**
     * 
     * @return
     *     The currentServerTime
     */
    public String getCurrentServerTime() {
        return currentServerTime;
    }

    /**
     * 
     * @param currentServerTime
     *     The CurrentServerTime
     */
    public void setCurrentServerTime(String currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The Type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The journeyProduct
     */
    public Integer getJourneyProduct() {
        return journeyProduct;
    }

    /**
     * 
     * @param journeyProduct
     *     The JourneyProduct
     */
    public void setJourneyProduct(Integer journeyProduct) {
        this.journeyProduct = journeyProduct;
    }

    /**
     * 
     * @return
     *     The transportSymbol
     */
    public String getTransportSymbol() {
        return transportSymbol;
    }

    /**
     * 
     * @param transportSymbol
     *     The TransportSymbol
     */
    public void setTransportSymbol(String transportSymbol) {
        this.transportSymbol = transportSymbol;
    }

}
