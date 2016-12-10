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
    NativeModules,
    StyleSheet,
    Text,
    TouchableHighlight,
    View
} from 'react-native';
import Geofence from './Geofence';

export default class RemoveGeofence extends Component {
    constructor(props) {
        super(props);
        this.state = {
            geofences: [],
            region: {
                latitude: 37.78825,
                longitude: -122.4324,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
            },
        };
        this.onMapClick = this.onMapClick.bind(this);
        this._onPress = this._onPress.bind(this);
        this.updateGeofences = this.updateGeofences.bind(this);

        this.updateGeofences();
    }



    render() {
        var t = this.state;
        return (
            <View style={styles.container}>
                <MapView
                    style={styles.map}
                    showsUserLocation={true}
                    onPress={this.onMapClick}
                    >
                    {this.state.geofences.map((g) =>
                        <MapView.Marker
                            coordinate={g.latlng}
                            title={g.destination + ", " + g.lineNumber}
                            key={g.requestId}
                            onPress={() => this._onPress(g)}
                            >
                            <TouchableHighlight underlayColor='#dddddd'>
                                <View style={styles.calloutText}>
                                    <Text style={styles.markerText}>{g.destination + ", " + g.lineNumber}</Text>
                                </View>
                            </TouchableHighlight>
                        </MapView.Marker>
                    )}
                </MapView>
            </View>
        );
    }

    _onPress(event) {
        console.log(event)
        Geofence.removeGeofence(event.requestId, () => {
            this.updateGeofences();
        });
    }

    updateGeofences() {
        Geofence.getGeofences((array) => {
            this.state.geofences = array.map((g) => {
                var s = g.split("|");
                var o = { siteId: s[0], destination: s[1], lineNumber: s[2], latlng: { latitude: parseFloat(s[3]), longitude: parseFloat(s[4]) }, requestId: g };
                return o;
            });
            this.setState(this.state);
        });
    }

    onMapClick(event) {
        //this.state.location = event.nativeEvent.coordinate;
        //this.state.markers.latlng = this.state.location;
        //this.setState(this.state);
    }

    createGeofence() {
        var coordinate = this.state.location;
        if (!coordinate || !this.state.settings.selectedStation || !this.state.settings.selectedLine
        ) {
            return;
        }
        var geo = NativeModules.GeofenceAndroid;
        var id = "geoTwo";
        var lat = coordinate.latitude;
        var lon = coordinate.longitude;
        var rad = 100;
        geo.registerGeofence(lat, lon, rad, this.state.settings.selectedStation.SiteId.toString(), this.state.settings.selectedLine.Destination, this.state.settings.selectedLine.LineNumber.toString());
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#F5FCFF',
    },
    markerText: {
        fontSize: 30,
        color: '#fff',
        backgroundColor: '#efefef'
    },
    map: {
        flex: 0.5
    },
    picker: {
        flex: 1
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});