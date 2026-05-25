if(TARGET mbgl-vendor-supercluster)
    return()
endif()

add_library(mbgl-vendor-supercluster INTERFACE)

target_include_directories(
    mbgl-vendor-supercluster SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/supercluster/include
)

set_target_properties(
    mbgl-vendor-supercluster
    PROPERTIES
        INTERFACE_MAPVINA_NAME "supercluster.hpp"
        INTERFACE_MAPVINA_URL "https://github.com/mapbox/supercluster.hpp"
        INTERFACE_MAPVINA_AUTHOR "Mapbox"
        INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/supercluster/LICENSE
)
