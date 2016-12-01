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
        this.state = { stations: [], transportLines: [] };
        this._onTextChange = this._onTextChange.bind(this);
        this._onStationPress = this._onStationPress.bind(this);
        this.getLineStyle = this.getLineStyle.bind(this);
    }

    render() {
        // Create function for sending along the pressed station
        var stationPress = (station) => () => this._onStationPress(station);
        var linePress = (line) => () => this._onLinePress(line);
        var selectedStation = null;


        return (
            <View style={styles.container}>
                <TextInput style={styles.stationName} onChangeText={this._onTextChange} />
                <ScrollView style={styles.transportModes}>
                    {
                        this.state.stations.map((station) => (
                            <TouchableHighlight style={this.state.selectedStation && station.Name === this.state.selectedStation.Name ? styles.selected : null} key={station.Name} onPress={stationPress(station)}>
                                <Text style={styles.stations}>{station.Name}</Text>
                            </TouchableHighlight>
                        ))
                    }
                </ScrollView>

                <ScrollView style={styles.transportLines}>
                    {
                        this.state.transportLines.map((group) => {
                            return group.data.map((obj) => {
                                return (
                                    <View>
                                        <Text style={styles.typeHeader}>{obj.Type || (group.name.indexOf('Metro') != -1 ? 'Tunnelbana' : group.Name)}</Text>
                                        {
                                            obj.uniqueLines.map((line) => (
                                                <TouchableHighlight style={this.getLineStyle(line)} key={line.Destination + line.LineNumber} onPress={linePress(line)}>
                                                    <View style={styles.transportLine}>
                                                        <Text style={styles.line}>{line.LineNumber}</Text>
                                                        <Text style={styles.line}>{line.Destination}</Text>
                                                    </View>
                                                </TouchableHighlight>
                                            ))
                                        }
                                    </View>
                                )
                            })
                        })
                    }
                </ScrollView>

            </View>
        );
    }

    getLineStyle(line) {
        var selected = this.state.selectedLine;
        // Return if no selected line exist
        if (!selected) return null;
        var lineSum = line.Destination + line.LineNumber;
        var selectedSum = selected.Destination + selected.LineNumber
        return selected && lineSum === selectedSum ? styles.selected : null;
    }

    async _onTextChange(text) {
        this.state.stationName = text;
        if (this.state.transportLines) {
            this.state.transportLines = [];
        }
        if (text.length > 2) {
            let url = 'http://sl.se/api/TypeAhead/Find/' + text;
            let response = await fetch(url);
            let responseJson = await response.json();
            this.state.stations = responseJson.data.filter((v) => v.SiteId != 0);
        }
        this.setState(this.state);
    }

    _onLinePress(line) {
        this.props.settings.selectedLine = line;
        this.state.selectedLine = line;
        this.setState(this.state);
    }

    async _onStationPress(station) {
        // Set selected station to state to mark the station in the list
        this.state.selectedStation = station;
        // Also set it in props to make it available to the parent component
        this.props.settings.selectedStation = station;
        let url = 'http://sl.se/api/sv/RealTime/GetDepartures/' + station.SiteId;
        let response = await fetch(url);
        let responseJson = await response.json();
        this.state.transportLines = this.parseDepartures(responseJson);

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
    selected: {
        borderWidth: 2,
    },
    typeHeader: {
        color: '#fff',
        fontSize: 26
    },
    transportLine: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        margin: 10,
    },
    line: {
        color: '#fff',
        fontSize: 20,
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