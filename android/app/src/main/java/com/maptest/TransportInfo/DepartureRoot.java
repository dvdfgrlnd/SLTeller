
package com.maptest.TransportInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartureRoot {

    @SerializedName("ms")
    @Expose
    private Integer ms;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * 
     * @return
     *     The ms
     */
    public Integer getMs() {
        return ms;
    }

    /**
     * 
     * @param ms
     *     The ms
     */
    public void setMs(Integer ms) {
        this.ms = ms;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }

}
