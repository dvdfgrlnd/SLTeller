/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import MapView from 'react-native-maps';
import {
    AppRegistry,
    NativeModules,
    StyleSheet,
    Text,
    View
} from 'react-native';
import StationPicker from './StationPicker';

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
            }
        };
        this.onMapClick = this.onMapClick.bind(this);
    }

    render() {
        return (
            <View style={styles.container}>
                <MapView
                    style={styles.map}
                    initialRegion={{
                        latitude: 37.78825,
                        longitude: -122.4324,
                        latitudeDelta: 0.0922,
                        longitudeDelta: 0.0421,
                    }}
                    showsUserLocation={true}
                    onPress={this.onMapClick}
                    >
                    <MapView.Marker
                        coordinate={this.state.markers.latlng}
                        title={this.state.markers.title}
                        description={this.state.markers.description}
                        key={this.state.markers.title}
                        />
                </MapView>
                <StationPicker style={styles.picker} />
            </View>
        );
    }

    onMapClick(event) {
        this.state.location = event.nativeEvent.coordinate;
    }

    createGeofence() {
        var coordinate = this.state.location;
        var geo = NativeModules.GeofenceAndroid;
        var id = "geoTwo";
        var lat = coordinate.latitude;
        var lon = coordinate.longitude;
        var rad = 100;
        geo.registerGeofence(id, lat, lon, rad);
        this.state.markers.latlng = coordinate;
        this.setState(this.state);
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