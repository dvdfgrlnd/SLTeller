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
    View
} from 'react-native';

export default class RemoveGeofence extends Component {
    constructor(props) {
        super(props);
        this.state = {
            markers: {
                latlng: {
                    latitude: 37.78833,
                    longitude: -122.4324,
                },
                title: "geofence",
                description: "geofence center",
            },
            settings: {},
            region: {
                latitude: 37.78825,
                longitude: -122.4324,
                latitudeDelta: 0.0922,
                longitudeDelta: 0.0421,
            },
            radius: 100,
        };
        this.onMapClick = this.onMapClick.bind(this);
        this._onButtonClick = this._onButtonClick.bind(this);
        this._onSettingsChange = this._onSettingsChange.bind(this);

        Geofence.getGeofences((array) => {
            this.state.geofences = array.map((g) => {
                var s = g.split("|");

            });
        });
    }

    render() {
        var t = this.state;
        var disableButton = !(t.settings.selectedLine && t.settings.selectedStation && t.location);
        return (
            <View style={styles.container}>
                <MapView
                    style={styles.map}
                    showsUserLocation={true}
                    onPress={this.onMapClick}
                    >
                    <MapView.Marker
                        coordinate={this.state.markers.latlng}
                        title={this.state.markers.title}
                        description={this.state.markers.description}
                        key={this.state.markers.title}
                        />
                    <MapView.Circle
                        center={this.state.markers.latlng}
                        radius={this.state.radius}
                        strokeWidth={10}>
                    </MapView.Circle>
                </MapView>

                <Button disabled={disableButton} onPress={this._onButtonClick} title="create geofence" />
            </View>
        );
    }

    _onSettingsChange() {
        // Rerender the component for potential change in settings
        this.setState(this.state);
    }

    _onButtonClick() {
        this.createGeofence();
    }

    onMapClick(event) {
        this.state.location = event.nativeEvent.coordinate;
        this.state.markers.latlng = this.state.location;
        this.setState(this.state);
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