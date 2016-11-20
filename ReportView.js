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
    StyleSheet,
    Text,
    TouchableHighlight,
    View
} from 'react-native';

export default class WeatherMap extends Component {
    constructor(props) {
        super(props);
        this.state = {
            weatherList: [{ file: require('./icons/Cloudy.png'), name: 'cloudy' }, { file: require('./icons/Sunny.png'), name: 'sunny' }, { file: require('./icons/Rainy.png'), name: 'rainy' }, { file: require('./icons/Snowy.png'), name: 'snowy' },
            ],
            weather: { selected: null },
        };
        this.sendReport = this.sendReport.bind(this);
    }

    render() {
        return (
            <View style={styles.container}>
                <IconRow style={styles.icons} icons={this.state.weatherList} checked={this.state.weather} />
                <Button
                    title="Press"
                    onPress={this.sendReport}
                    color="#343434" />
            </View>
        );
    }

    sendReport() {
        console.log(this.state.weather.selected);
        var weather = {};
        weather.sky = this.state.weather.selected;
        var url = 'http://weathersoon.herokuapp.com';
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(
                weather)
        })

    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#239EFF',
    },
    map: {
        flex: 1
    },
    icons: {
        flex: 1,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});