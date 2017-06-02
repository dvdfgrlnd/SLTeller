
package com.teller.TransportInfo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetroGroup {

    @SerializedName("Departures")
    @Expose
    private List<Departure__> departures = new ArrayList<Departure__>();
    @SerializedName("InformationMessage")
    @Expose
    private Object informationMessage;
    @SerializedName("StopPointDeviations")
    @Expose
    private List<Object> stopPointDeviations = new ArrayList<Object>();
    @SerializedName("CurrentServerTime")
    @Expose
    private Object currentServerTime;
    @SerializedName("Type")
    @Expose
    private Object type;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("JourneyProduct")
    @Expose
    private Integer journeyProduct;
    @SerializedName("TransportSymbol")
    @Expose
    private Object transportSymbol;

    /**
     * 
     * @return
     *     The departures
     */
    public List<Departure__> getDepartures() {
        return departures;
    }

    /**
     * 
     * @param departures
     *     The Departures
     */
    public void setDepartures(List<Departure__> departures) {
        this.departures = departures;
    }

    /**
     * 
     * @return
     *     The informationMessage
     */
    public Object getInformationMessage() {
        return informationMessage;
    }

    /**
     * 
     * @param informationMessage
     *     The InformationMessage
     */
    public void setInformationMessage(Object informationMessage) {
        this.informationMessage = informationMessage;
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
    public Object getCurrentServerTime() {
        return currentServerTime;
    }

    /**
     * 
     * @param currentServerTime
     *     The CurrentServerTime
     */
    public void setCurrentServerTime(Object currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

    /**
     * 
     * @return
     *     The type
     */
    public Object getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The Type
     */
    public void setType(Object type) {
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
    public Object getTransportSymbol() {
        return transportSymbol;
    }

    /**
     * 
     * @param transportSymbol
     *     The TransportSymbol
     */
    public void setTransportSymbol(Object transportSymbol) {
        this.transportSymbol = transportSymbol;
    }

}
