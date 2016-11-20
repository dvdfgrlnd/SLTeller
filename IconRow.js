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

export default class IconRow extends Component {
    constructor(props) {
        super(props);
        this.state = {
        };
        this._onChange = this._onChange.bind(this);
    }

    render() {
        var onChange = (weather) => () => this._onChange(weather)
        return (
            <View style={styles.weatherContainer}>
                {
                    this.props.icons.map((icon) =>
                        (
                            <TouchableHighlight onPress={onChange(icon)} key={icon.name} style={styles.touch}>
                                <Image
                                    style={styles.icon}
                                    resizeMode={'contain'}
                                    source={icon.file} />
                            </TouchableHighlight>
                        )
                    )
                }
            </View>
        );
    }

    _onChange(weather) {
        if (this.props.checked.selected == weather) {
            this.props.checked.selected = null;
        } else {
            this.props.checked.selected = weather;
        }
        //console.log(this.props.checked.selected);
    }
}

const styles = StyleSheet.create({
    weatherContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'space-between',
        backgroundColor: '#239EFF',
    },
    touch: {
        flex: 0
    },
    icon: {
        width: 50,
        height: 50,
    },
});