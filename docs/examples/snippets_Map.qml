//! [Map plugin]
Plugin {
    id: mapPlugin
    name: "mapvina"

    PluginParameter {
        name: "mapvina.map.styles"
        value: "https://maps.mapvina.com/styles/v2/streets.json?key=public_key"
    }
}
//! [Map plugin]

//! [Style attachment]
MapView {
    id: mapView
    anchors.fill: parent
    map.plugin: mapPlugin

    map.zoomLevel: 5
    map.center: QtPositioning.coordinate(41.874, -75.789)

    MapVina.style: Style {
        id: style
    }
}
//! [Style attachment]
