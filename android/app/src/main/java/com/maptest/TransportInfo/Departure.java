
package com.maptest.TransportInfo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Departure implements IDeparture{

    @SerializedName("ExpectedDateTime")
    @Expose
    private String expectedDateTime;
    @SerializedName("Deviations")
    @Expose
    private List<Object> deviations = new ArrayList<Object>();
    @SerializedName("StopPointNumber")
    @Expose
    private String stopPointNumber;
    @SerializedName("DisplayTime")
    @Expose
    private String displayTime;
    @SerializedName("LineNumber")
    @Expose
    private String lineNumber;
    @SerializedName("Destination")
    @Expose
    private String destination;

    /**
     * 
     * @return
     *     The expectedDateTime
     */
    public String getExpectedDateTime() {
        return expectedDateTime;
    }

    /**
     * 
     * @param expectedDateTime
     *     The ExpectedDateTime
     */
    public void setExpectedDateTime(String expectedDateTime) {
        this.expectedDateTime = expectedDateTime;
    }

    /**
     * 
     * @return
     *     The deviations
     */
    public List<Object> getDeviations() {
        return deviations;
    }

    /**
     * 
     * @param deviations
     *     The Deviations
     */
    public void setDeviations(List<Object> deviations) {
        this.deviations = deviations;
    }

    /**
     * 
     * @return
     *     The stopPointNumber
     */
    public String getStopPointNumber() {
        return stopPointNumber;
    }

    /**
     * 
     * @param stopPointNumber
     *     The StopPointNumber
     */
    public void setStopPointNumber(String stopPointNumber) {
        this.stopPointNumber = stopPointNumber;
    }

    /**
     * 
     * @return
     *     The displayTime
     */
    public String getDisplayTime() {
        return displayTime;
    }

    /**
     * 
     * @param displayTime
     *     The DisplayTime
     */
    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    /**
     * 
     * @return
     *     The lineNumber
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * 
     * @param lineNumber
     *     The LineNumber
     */
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * 
     * @return
     *     The destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * 
     * @param destination
     *     The Destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

}
