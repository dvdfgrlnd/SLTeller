/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import MapView from 'react-native-maps';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View
} from 'react-native';

export default class WeatherMap extends Component {
    constructor(props) {
        super(props);
        this.state = {
            markers: [
                {
                    latlng: {
                        latitude: 37.78833,
                        longitude: -122.4324,
                    },
                    title: "point one",
                    description: "one",
                },
                {
                    latlng: {
                        latitude: 37.78825,
                        longitude: -122.4332,
                    },
                    title: "point two",
                    description: "two",
                },
                {
                    latlng: {
                        latitude: 37.78837,
                        longitude: -122.4334,
                    },
                    title: "point three",
                    description: "three",
                },
                {
                    latlng: {
                        latitude: 37.77833,
                        longitude: -122.4324,
                    },
                    title: "point one",
                    description: "one",
                },
                {
                    latlng: {
                        latitude: 37.77825,
                        longitude: -122.4332,
                    },
                    title: "point two",
                    description: "two",
                },
                {
                    latlng: {
                        latitude: 37.77837,
                        longitude: -122.4334,
                    },
                    title: "point three",
                    description: "three",
                }
            ]
        };
        var clusters = [];
        var origin = [];
        for (i = 0; i < 2; i++) {
            c = {
                latlng: {
                    latitude: 37.77837,
                    longitude: -122.4334,
                },
                title: "origin " + i.toString(),
                description: "c" + i.toString(),
                key: "c" + i.toString(),
                elem: []
            }
            c.latlng.latitude += (Math.random() * 0.001);
            c.latlng.longitude += (Math.random() * 0.001);
            origin.push(JSON.parse(JSON.stringify(c)));
            c.title = "cluster " + i.toString();
            clusters.push(c);
        }
        for (i = 0; i < 10; i++) {
            // Reset cluster
            clusters.forEach((c) => c.elem = []);
            // Calculate and add nearest cluster
            this.state.markers.forEach((m) => {
                min = 1000000;
                minInd = 0;
                clusters.forEach((c, i) => {
                    dist = this.distance(c.latlng, m.latlng);
                    if (dist < min) {
                        min = dist;
                        minInd = i;
                    }
                });
                clusters[minInd].elem.push(m);
            });
            // Calculate center
            clusters.forEach((c) => {
                var sum = c.elem.reduce((p, e) => {
                    p.latitude += e.latlng.latitude;
                    p.longitude += e.latlng.longitude;
                    return p;
                }, { latitude: 0, longitude: 0 });
                sum.latitude /= c.elem.length;
                sum.longitude /= c.elem.length;
                c.latlng = sum;
            });
            //console.log(JSON.stringify(clusters));
        }
        //this.state.markers = this.state.markers.concat(clusters)
        this.state.markers = clusters;
        //this.state.markers = this.state.markers.concat(origin)
    }

    distance(latlon1, latlon2) {
        var p = Math.PI / 180
        var c = Math.cos;
        var a = 0.5 - c((latlon2.latitude - latlon1.latitude) * p) / 2 +
            c(latlon1.latitude * p) * c(latlon2.latitude * p) *
            (1 - c((latlon2.longitude - latlon1.longitude) * p)) / 2;

        return 12742000 * Math.asin(Math.sqrt(a))// 2 * R; R = 6371 km
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
                    >
                    {this.state.markers.map(marker => (
                        <MapView.Marker
                            coordinate={marker.latlng}
                            title={marker.title}
                            description={marker.description}
                            key={marker.title}
                            />
                    ))}
                </MapView>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#F5FCFF',
    },
    map: {
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