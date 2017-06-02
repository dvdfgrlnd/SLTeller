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
        this.reRegister = this.reRegister.bind(this);
        this.updateGeofences = this.updateGeofences.bind(this);

        this.updateGeofences();
    }



    render() {
        var t = this.state;
        return (
            <View style={styles.container}>
                <View>
                    <Button title="re-register" onPress={this.reRegister} />
                </View>
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

    reRegister(){
        Geofence.reRegisterGeofence((array) => {
            console.log("reregister");
        });
    }

    _onPress(event) {
        console.log(event)
        Geofence.removeGeofence(event.requestId, () => {
            this.updateGeofences();
        });
    }

    updateGeofences() {
        console.log('update');
        Geofence.getGeofences((array) => {
            this.state.geofences = array.map((obj) => {
                obj.latlng = { latitude: obj.latitude, longitude: obj.longitude };
                delete obj.latitude;
                delete obj.longitude;
                return obj;
            });
            this.setState(this.state);
        });
    }

    onMapClick(event) {
        //this.state.location = event.nativeEvent.coordinate;
        //this.state.markers.latlng = this.state.location;
        //this.setState(this.state);
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#fff',
    },
    markerText: {
        fontSize: 25,
        color: '#333',
        backgroundColor: '#fafafa',
        padding:5,
        borderColor: '#239eff',
        borderRadius:10,
        borderWidth: 2,
        borderStyle: 'solid',
    },
    calloutText:{
    },
    map: {
        flex: 1
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
        color: '#333',
        marginBottom: 5,
    },
});