
package com.maptest.TransportInfo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("BusGroups")
    @Expose
    private List<BusGroup> busGroups = new ArrayList<BusGroup>();
    @SerializedName("TrainGroups")
    @Expose
    private List<TrainGroup> trainGroups = new ArrayList<TrainGroup>();
    @SerializedName("TranCityTypes")
    @Expose
    private List<Object> tranCityTypes = new ArrayList<Object>();
    @SerializedName("TramTypes")
    @Expose
    private List<Object> tramTypes = new ArrayList<Object>();
    @SerializedName("MetroGroups")
    @Expose
    private List<MetroGroup> metroGroups = new ArrayList<MetroGroup>();
    @SerializedName("ShipGroups")
    @Expose
    private List<Object> shipGroups = new ArrayList<Object>();
    @SerializedName("HasStopPointDeviations")
    @Expose
    private Boolean hasStopPointDeviations;
    @SerializedName("LastUpdate")
    @Expose
    private String lastUpdate;
    @SerializedName("Error")
    @Expose
    private Object error;
    @SerializedName("HasResultData")
    @Expose
    private Boolean hasResultData;

    /**
     * 
     * @return
     *     The busGroups
     */
    public List<BusGroup> getBusGroups() {
        return busGroups;
    }

    /**
     * 
     * @param busGroups
     *     The BusGroups
     */
    public void setBusGroups(List<BusGroup> busGroups) {
        this.busGroups = busGroups;
    }

    /**
     * 
     * @return
     *     The trainGroups
     */
    public List<TrainGroup> getTrainGroups() {
        return trainGroups;
    }

    /**
     * 
     * @param trainGroups
     *     The TrainGroups
     */
    public void setTrainGroups(List<TrainGroup> trainGroups) {
        this.trainGroups = trainGroups;
    }

    /**
     * 
     * @return
     *     The tranCityTypes
     */
    public List<Object> getTranCityTypes() {
        return tranCityTypes;
    }

    /**
     * 
     * @param tranCityTypes
     *     The TranCityTypes
     */
    public void setTranCityTypes(List<Object> tranCityTypes) {
        this.tranCityTypes = tranCityTypes;
    }

    /**
     * 
     * @return
     *     The tramTypes
     */
    public List<Object> getTramTypes() {
        return tramTypes;
    }

    /**
     * 
     * @param tramTypes
     *     The TramTypes
     */
    public void setTramTypes(List<Object> tramTypes) {
        this.tramTypes = tramTypes;
    }

    /**
     * 
     * @return
     *     The metroGroups
     */
    public List<MetroGroup> getMetroGroups() {
        return metroGroups;
    }

    /**
     * 
     * @param metroGroups
     *     The MetroGroups
     */
    public void setMetroGroups(List<MetroGroup> metroGroups) {
        this.metroGroups = metroGroups;
    }

    /**
     * 
     * @return
     *     The shipGroups
     */
    public List<Object> getShipGroups() {
        return shipGroups;
    }

    /**
     * 
     * @param shipGroups
     *     The ShipGroups
     */
    public void setShipGroups(List<Object> shipGroups) {
        this.shipGroups = shipGroups;
    }

    /**
     * 
     * @return
     *     The hasStopPointDeviations
     */
    public Boolean getHasStopPointDeviations() {
        return hasStopPointDeviations;
    }

    /**
     * 
     * @param hasStopPointDeviations
     *     The HasStopPointDeviations
     */
    public void setHasStopPointDeviations(Boolean hasStopPointDeviations) {
        this.hasStopPointDeviations = hasStopPointDeviations;
    }

    /**
     * 
     * @return
     *     The lastUpdate
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * 
     * @param lastUpdate
     *     The LastUpdate
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * 
     * @return
     *     The error
     */
    public Object getError() {
        return error;
    }

    /**
     * 
     * @param error
     *     The Error
     */
    public void setError(Object error) {
        this.error = error;
    }

    /**
     * 
     * @return
     *     The hasResultData
     */
    public Boolean getHasResultData() {
        return hasResultData;
    }

    /**
     * 
     * @param hasResultData
     *     The HasResultData
     */
    public void setHasResultData(Boolean hasResultData) {
        this.hasResultData = hasResultData;
    }

}
