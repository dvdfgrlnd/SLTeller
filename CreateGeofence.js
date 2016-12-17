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
    TextInput,
    View
} from 'react-native';
import StationPicker from './StationPicker';
import StatusRow from './StatusRow';
import Geofence from './Geofence';

export default class CreateGeofence extends Component {
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
        this.registerCallback = this.registerCallback.bind(this);
        this.removeCallback = this.removeCallback.bind(this);
        this._handleError = this._handleError.bind(this);
        this._onTextChanged = this._onTextChanged.bind(this);

    }

    render() {
        var t = this.state;
        var disableButton = !(t.settings.selectedLine && t.settings.selectedStation && t.location);
        // Set a timer to remove the status message after a while
        if (this.state.status) {
            var seconds = 12000;
            setTimeout(() => { delete this.state.status; this.setState(this.state); }, seconds);
        }
        return (
            <View style={styles.container}>
                <StatusRow message={this.state.status} />
                <View style={styles.radiusContainer}>
                    <Text style={styles.radiusText}>radius</Text>
                    <TextInput style={styles.radiusInput} value={this.state.radius.toString()} keyboardType='numeric' onChangeText={this._onTextChanged} />
                </View>
                <MapView
                    style={styles.map}
                    showsUserLocation={true}
                    onPress={this.onMapClick}
                    >
                    <MapView.Circle
                        center={this.state.markers.latlng}
                        radius={this.state.radius}
                        fillColor='#90BABABA'
                        strokeColor='#565656'
                        strokeWidth={3}>
                    </MapView.Circle>
                </MapView>
                <StationPicker style={styles.picker} settings={this.state.settings} onChange={this._onSettingsChange} error={this._handleError} />

                <Button disabled={disableButton} onPress={this._onButtonClick} title="create geofence" />
            </View>
        );
    }

    _onTextChanged(text) {
        this.state.radius = parseFloat(text);
        this.setState(this.state);
    }

    _handleError(error) {
        this.state.status = { type: 'error', text: error };
        this.setState(this.state);
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
        var id = "geoTwo";
        var lat = coordinate.latitude;
        var lon = coordinate.longitude;
        var rad = this.state.radius;
        var siteId = this.state.settings.selectedStation.SiteId.toString();
        var destination = this.state.settings.selectedLine.Destination;
        var lineNumber = this.state.settings.selectedLine.LineNumber.toString();
        Geofence.registerGeofence(lat, lon, rad, siteId, destination, lineNumber, this.registerCallback);
    }

    registerCallback(status) {
        if (status === 0)
            this.state.status = { type: 'ok', text: "successfully created geofence!" };
        else
            this.state.status = { type: 'error', text: "error creating the geofence" };

        this.setState(this.state);
    }

    removeCallback(status) {
        if (status === 0)
            this.state.status = { type: 'ok', text: "successfully removed geofence!" };
        else
            this.state.status = { type: 'error', text: "error removing the geofence" };

        this.setState(this.state);
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#F5FCFF',
    },
    radiusContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        margin: 3,
    },
    radiusInput: {
        flex: 3,
        height: 40,
        backgroundColor: '#F5FCFF',
    },
    radiusText: {
        fontSize: 23,

        flex: 1,
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