// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "MapVina Native",
    products: [
        .library(
            name: "Mapbox",
            targets: ["Mapbox"]
        ),
    ],
    dependencies: [
    ],
    targets: [
        .binaryTarget(
            name: "Mapbox",
            url: "MAPVINA_PACKAGE_URL",
            checksum: "MAPVINA_PACKAGE_CHECKSUM"
        ),
    ]
)
