/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import WeatherMap from './WeatherMap';
import ReportView from './ReportView';
import {
    AppRegistry,
    StyleSheet,
    Navigator,
    Text,
    TouchableHighlight,
    View
} from 'react-native';

export default class App extends Component {

    constructor(props) {
        super(props);
        this.routeNames = { map: 0, report: 1 };
        this.routes = [{ id: this.routeNames.report }, { id: this.routeNames.map }];
        this.state = {};
        this.renderScene = this.renderScene.bind(this);
    }

    render() {
        return (
            <Navigator
                initialRoute={this.routes[0]}
                initialRouteStack={this.routes}
                renderScene={this.renderScene}
                />
        );
    }

    renderScene(route, navigator) {
        var view;
        switch (route.id) {
            case this.routeNames.map:
                view = (<WeatherMap />);
                break;
            case this.routeNames:
                view = <ReportView />
                break;
        }
        var onChange = (id) => {
            var index = this.state.routes.findIndex((d) => d.index === route.id);
            var currentRoute = this.state.routes[index];
            if (id === 0 && currentRoute.id !== 0) {
                navigator.pop();
            } else if (id === 1 && currentRoute.id !== 1) {
                navigator.push(this.state.routes[1]);
            }
        }
        changeToMap = () => onChange(this.routeNames.map);
        changeToReport = () => onChange(this.routeNames.report);
        return (
            <View style={styles.container} >
                <View style={styles.navbar}>
                    <TouchableHighlight style={styles.navbutton} onPress={changeToMap}>
                        <Text style={styles.menuText}>Map</Text>
                    </TouchableHighlight>
                    <TouchableHighlight style={styles.navbutton} onPress={changeToReport}>
                        <Text style={styles.menuText}>Report</Text>
                    </TouchableHighlight>
                </View >
                {view}
            </View >
        )
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'column',
        backgroundColor: '#F5FCFF',
    },
    navbutton: {
        flex: 1,
    },
    navbar: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        backgroundColor: '#F5FCFF',
    },
    map: {
        flex: 1
    },
    menuText: {
        fontSize: 20,
        textAlign: 'center',
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});