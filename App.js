/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import CreateGeofence from './CreateGeofence';
import ReportView from './StationPicker';
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
        this.routes = [{ id: this.routeNames.map }, { id: this.routeNames.report },];
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
                view = (<CreateGeofence />);
                break;
            case this.routeNames.report:
                view = <ReportView />
                break;
            default:
                view = (<CreateGeofence />);
                break;
        }
        var onChange = (id) => {
            var index = this.routes.findIndex((d) => d.id === id);
            var newRoute = this.routes[index];
            navigator.jumpTo(newRoute);
        }
        changeToMap = () => onChange(this.routeNames.map);
        changeToReport = () => onChange(this.routeNames.report);
        return (
            <View style={styles.container} >
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
/*
                <View style={styles.navbar}>
                    <TouchableHighlight style={styles.navbutton} onPress={changeToMap}>
                        <Text style={styles.menuText}>Map</Text>
                    </TouchableHighlight>
                    <TouchableHighlight style={styles.navbutton} onPress={changeToReport}>
                        <Text style={styles.menuText}>Report</Text>
                    </TouchableHighlight>
                </View >
*/