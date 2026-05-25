if(TARGET mbgl-vendor-calloutview)
    return()
endif()

add_library(
    mbgl-vendor-calloutview ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView/SMCalloutView.m
)

target_include_directories(
    mbgl-vendor-calloutview SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView
)

set_target_properties(
    mbgl-vendor-calloutview
    PROPERTIES
        INTERFACE_MAPVINA_NAME "Boost C++ Libraries"
        INTERFACE_MAPVINA_URL "https://github.com/nfarina/calloutview"
        INTERFACE_MAPVINA_AUTHOR "Nick Farina"
        INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView/LICENSE
)
