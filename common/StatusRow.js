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
    StyleSheet,
    Text,
    TouchableHighlight,
    View
} from 'react-native';

export default class StatusRow extends Component {
    constructor(props) {
        super(props);
        this.state = {
        };
    }

    render() {
        var type, text;
        if (this.props.message) {
            type = this.props.message.type === 'error' ? styles.errorText : styles.okText;
            text = this.props.message.text;
        }
        return (
            <View style={this.props.message ? styles.weatherContainer : { height: 0 }}>
                <Text style={[styles.statusText, type]}>{text || '   '}</Text>
            </View>
        );
    }
}
//        backgroundColor: '#0DC3FF',
const styles = StyleSheet.create({
    weatherContainer: {
        backgroundColor: '#fff',
    },
    statusText: {
        fontSize: 23,
        textAlign: 'center'
    },
    errorText: {
        color: '#FF0000',
    },
    okText: {
        color: '#1FBA01',
    }
});