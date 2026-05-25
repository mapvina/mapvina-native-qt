if(TARGET mbgl-vendor-wagyu)
    return()
endif()

add_library(
    mbgl-vendor-wagyu INTERFACE
)

target_include_directories(
    mbgl-vendor-wagyu SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/wagyu/include
)

set_target_properties(
    mbgl-vendor-wagyu
    PROPERTIES
        INTERFACE_MAPVINA_NAME "wagyu"
        INTERFACE_MAPVINA_URL "https://github.com/mapbox/wagyu.git"
        INTERFACE_MAPVINA_AUTHOR "Angus Johnson and Mapbox"
        INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/wagyu/LICENSE
)
