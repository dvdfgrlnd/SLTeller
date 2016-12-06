/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import CreateGeofence from './CreateGeofence';
import RemoveGeofence from './RemoveGeofence';
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
        this.routeNames = { create: 0, remove: 1 };
        this.routes = [{ id: this.routeNames.create }, { id: this.routeNames.remove },];
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
            case this.routeNames.create:
                view = (<CreateGeofence />);
                break;
            case this.routeNames.remove:
                view = <RemoveGeofence />
                break;
            default:
                view = (<CreateGeofence />);
                route.id = this.routeNames.create;
                break;
        }
        var onChange = (id) => {
            var index = this.routes.findIndex((d) => d.id === id);
            var newRoute = this.routes[index];
            navigator.jumpTo(newRoute);
        }
        changeToMap = () => onChange(this.routeNames.create);
        changeToReport = () => onChange(this.routeNames.remove);
        var names = this.routeNames;
        return (
            <View style={styles.container} >
                <View style={styles.navbar}>
                    <TouchableHighlight style={(route.id === names.create) ? styles.selectedNavbutton : styles.navbutton} onPress={changeToMap}>
                        <Text style={styles.menuText}>Create</Text>
                    </TouchableHighlight>
                    <TouchableHighlight style={(route.id === names.remove) ? styles.selectedNavbutton : styles.navbutton} onPress={changeToReport}>
                        <Text style={styles.menuText}>Remove</Text>
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
        margin: 10,
    },
    selectedNavbutton: {
        flex: 2,
        padding: 10,
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