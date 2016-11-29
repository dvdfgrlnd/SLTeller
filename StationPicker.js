/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import MapView from 'react-native-maps';
import IconRow from './IconRow'
import {
    AppRegistry,
    Button,
    Image,
    NativeModules,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    TouchableHighlight,
    View
} from 'react-native';

export default class StationPicker extends Component {
    constructor(props) {
        super(props);
        this.state = { stations: [] };
        this._onTextChange = this._onTextChange.bind(this);
        this._onStationPress = this._onStationPress.bind(this);
    }

    render() {
        // Create function for sending along the pressed station
        var stationPress = (station) => () => this._onStationPress(station);
        var selectedStation = null;
        if (this.state.selectedStation) {
            selectedStation = (
                <View style={styles.selectionContainer}>
                    <Text style={styles.selectionItem}>Selected:</Text>
                    <Text style={styles.selectionItem}>{this.state.selectedStation.Name}</Text>
                </View>
            );
        }

        return (
            <View style={styles.container}>
                <TextInput style={styles.stationName} onChangeText={this._onTextChange} />
                <ScrollView style={styles.transportModes}>
                    {
                        this.state.stations.map((station) => (
                            <TouchableHighlight key={station.Name} onPress={stationPress(station)}>
                                <Text style={styles.stations}>{station.Name}</Text>
                            </TouchableHighlight>
                        ))
                    }
                </ScrollView>

                {selectedStation}

            </View>
        );
    }

    async _onTextChange(text) {
        this.state.stationName = text;
        if (text.length > 2) {
            let url = 'http://sl.se/api/TypeAhead/Find/' + text;
            let response = await fetch(url);
            let responseJson = await response.json();
            this.state.stations = responseJson.data;
        }
        this.setState(this.state);
    }

    _onStationPress(station) {
        this.state.selectedStation = station;
        // Set stations to empty to "disable" station list
        this.state.stations = [];
        this.setState(this.state);
    }

    parseDepartures(json) {
        var keys = Object.keys(json.data);
        var transportGroups = [];
        keys.forEach((k) => {
            if (Array.isArray(json.data[k])) {
                transportGroups.push({ data: json.data[k], name: k });
            }
        });
        transportGroups.forEach((group) => {
            group.data.forEach((obj) => {
                var dep = obj.Departures;
                // Find unique lines
                var lines = dep.reduce((p, c) => {
                    var index = p.findIndex((e) => (e.Destination === c.Destination && e.LineNumber === c.LineNumber));
                    if (index === -1) {
                        p.push(c);
                    }
                    return p;
                }, []);
                obj.uniqueLines = lines;
            });
        });
        return transportGroups;
    }


    sendReport() {
        console.log(this.state.weather.selected);
        var weather = {};
        weather.sky = this.state.weather.selected;
        //var url = 'http://weathersoon.herokuapp.com';
        var url = 'localhost:5000/weather/';
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                weather)
        });
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#239EFF',
    },
    stationName: {
        color: '#fff'
    },
    selectionContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    selectionItem: {
        color: '#fff',
        fontSize: 23,
    },
    stations: {
        color: '#fff',
        fontSize: 25,
        textAlign: 'center'
    }
});