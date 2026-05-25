import QtQuick 2.15
import QtLocation 6.5
import QtPositioning 6.5

import MapVina.Location 4.0

Item {
    width: 512
    height: 512

    Plugin {
        id: mapPlugin
        name: "mapvina"

        PluginParameter {
            name: "mapvina.map.styles"
            value: "https://maps.mapvina.com/styles/v2/streets.json?key=public_key"
        }
    }

    MapView {
        id: mapView
        anchors.fill: parent
        map.plugin: mapPlugin

        map.zoomLevel: 5
        map.center: QtPositioning.coordinate(41.874, -75.789)

        MapVina.style: Style {
            id: style

            SourceParameter {
                id: radarSourceParam
                styleId: "radar"
                type: "image"
                property string url: "https://mapvina.com/mapvina-gl-js/docs/assets/radar1.gif"
                property var coordinates: [
                    [-80.425, 46.437],
                    [-71.516, 46.437],
                    [-71.516, 37.936],
                    [-80.425, 37.936]
                ]
            }

            LayerParameter {
                id: radarLayerParam
                styleId: "radar-layer"
                type: "raster"
                property string source: "radar"

                paint: {
                    "raster-opacity": 0.9
                }
            }
        }
    }
}
