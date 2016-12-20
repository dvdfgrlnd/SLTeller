/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import MapView from 'react-native-maps';
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
        console.log(this.props.settings);

        return (
            <ScrollView style={styles.container}>
                <TextInput style={styles.stationName} onChangeText={this._onTextChange} />
                {(() => {
                    return this.props.settings.length > 0 ? <Text style={styles.typeHeader}>Selected</Text> : undefined;
                })()}
                <View style={styles.chosen}>
                    {
                        this.props.settings.map((item, i) => (
                            <View style={styles.selectedDepartures} key={item.line.Destination + item.station.Name}>
                                <Text style={styles.line}>{item.station.Name}</Text>
                                <Text style={styles.line}>{item.line.Destination}</Text>
                                <Text style={styles.line}>{item.line.LineNumber}</Text>
                                <Button onPress={() => this._onRemove(i)} title='X' />
                            </View>
                        ))
                    }
                </View>
                {(() => {
                    return this.state.selectedStation ? <Text style={styles.typeHeader}>Stations</Text> : undefined;
                })()
                }

                <View style={styles.transportModes}>
                    {
                        this.state.stations.map((station) => (
                            <TouchableHighlight style={this.state.selectedStation && station.Name === this.state.selectedStation.Name ? styles.selected : null} key={station.Name} onPress={stationPress(station)}>
                                <Text style={styles.stations}>{station.Name}</Text>
                            </TouchableHighlight>
                        ))
                    }
                </View>

                <View style={styles.transportLines}>
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
                                );
                            });
                        })
                    }
                </View>

            </ScrollView>
        );
    }

    _onRemove(index) {
        this.props.settings.splice(index, 1);
        // Send event to parent component to indicate change in settings
        this.props.onChange();
        this.setState(this.state);
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
            try {
                let response = await fetch(url);
                let responseJson = await response.json();
                // Filter this due to some stations having same name but different SiteId which makes the list complain about multiple items with the same key (i.e. the name), could use the siteId in conjunction with name, but that would be confusing since you then have two items with the same name.
                this.state.stations = responseJson.data.filter((v) => v.SiteId != 0).reduce((p, c) => p.findIndex((f) => this._isStationsEqual(f, c)) !== -1 ? p : p.concat(c), []);
            } catch (err) {
                console.log(err);
                this.props.error("error fetching stations");
            }
        }
        this.setState(this.state);
    }

    _isStationsEqual(a, b) {
        return a.Name === b.Name;
    }

    _onLinePress(line) {
        // Send event to parent component to indicate change in settings
        this.props.onChange();
        this.state.selectedLine = line;

        // Add station and line to selectedDepartures
        this.props.settings.push({ line, station: this.state.selectedStation });

        // Send event to parent component to indicate change in settings
        this.props.onChange();

        this.setState(this.state);
    }

    async _onStationPress(station) {
        // Set selected station to state to mark the station in the list
        this.state.selectedStation = station;
        let url = 'http://sl.se/api/sv/RealTime/GetDepartures/' + station.SiteId;
        try {
            let response = await fetch(url);
            let responseJson = await response.json();
            this.state.transportLines = this.parseDepartures(responseJson);
        } catch (err) {
            this.props.error("error fetching transport groups");
        }

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
                if (!dep) {
                    var name;
                    if (obj.TramGroups)
                        name = 'TramGroups';
                    else if (obj.TranCityGroups)
                        name = 'TranCityGroups';
                    else
                        return

                    dep = obj[name].reduce((p, c) => {
                        return p.concat(c.Departures);
                    }, []);
                }
                // Find unique lines
                var lines = dep.reduce((p, c) => {
                    var index = p.findIndex((e) => (e.Destination === c.Destination && e.LineNumber === c.LineNumber));
                    if (index === -1) {
                        p.push(c);
                    }
                    return p;
                }, []);
                obj.uniqueLines = lines.sort(this.compare);
            });
        });
        return transportGroups;
    }

    compare(a, b) {
        var diff = parseInt(a.LineNumber.replace(/\D/g, '')) - parseInt(b.LineNumber.replace(/\D/g, ''));
        if (diff < 0) {
            return -1;
        }
        if (diff > 0) {
            return 1;
        }
        // a must be equal to b
        return 0;
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#239EFF',
    },
    stationName: {
        color: '#000',
        fontSize: 20,
        height: 40,
        backgroundColor: '#F5FCFF',
    },
    selected: {
        borderWidth: 2,
    },
    typeHeader: {
        color: '#fff',
        fontSize: 24
    },
    selectedDepartures: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        margin: 10,
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
        fontSize: 20,
    },
    stations: {
        color: '#fff',
        fontSize: 20,
        textAlign: 'center'
    }
});