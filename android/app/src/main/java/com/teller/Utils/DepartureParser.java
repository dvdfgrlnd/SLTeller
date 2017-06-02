package com.teller.Utils;

import com.google.gson.Gson;
import com.teller.GeofenceAndroid.Departure;
import com.teller.GeofenceAndroid.Result;
import com.teller.TransportInfo.BusGroup;
import com.teller.TransportInfo.Data;
import com.teller.TransportInfo.DepartureRoot;
import com.teller.TransportInfo.IDeparture;
import com.teller.TransportInfo.MetroGroup;
import com.teller.TransportInfo.TrainGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.teller.Utils.TimeParser.parseMin;

/**
 * Created by david on 12/19/16.
 */

public class DepartureParser {

    public static int findEarliestDeparture(Result res) {
        Gson gson = new Gson();
        int minTime = 0;
        try {
            DepartureRoot dr = gson.fromJson(res.getResponse(), DepartureRoot.class);
            List<IDeparture> list = new ArrayList<>();
            Data data = dr.getData();
            for (BusGroup group : dr.getData().getBusGroups()) {
                list.addAll(group.getDepartures());
            }
            for (MetroGroup group : dr.getData().getMetroGroups()) {
                list.addAll(group.getDepartures());
            }
            for (TrainGroup group : dr.getData().getTrainGroups()) {
                list.addAll(group.getDepartures());
            }
            List<IDeparture> filteredDepartures = new ArrayList<>();
            for (IDeparture idep : list) {
                // Iterate all departures for this station
                for (Departure dep : res.getDepartureList()) {
                    if (idep.getDestination().equals(dep.getDestination()) && idep.getLineNumber().equals(dep.getLineNumber())) {
                        filteredDepartures.add(idep);
                        break;
                    }
                }
            }
            Collections.sort(filteredDepartures, new Comparator<IDeparture>() {
                @Override
                public int compare(IDeparture iDeparture, IDeparture t1) {
                    int diff = parseMin(iDeparture.getDisplayTime()) - parseMin(t1.getDisplayTime());
                    if (diff < 0)
                        return -1;
                    else if (diff > 0)
                        return 1;
                    else
                        return 0;
                }
            });
            minTime = parseMin(filteredDepartures.get(0).getDisplayTime());
        } catch (Exception e) {
        }
        return minTime;
    }

}
