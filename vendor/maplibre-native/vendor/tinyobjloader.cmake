include(FetchContent)

FetchContent_Declare(
    tinyobjloader
    GIT_REPOSITORY "https://github.com/tinyobjloader/tinyobjloader.git"
    GIT_TAG release
    GIT_SHALLOW TRUE
    GIT_PROGRESS TRUE
)

FetchContent_MakeAvailable(tinyobjloader)

set_target_properties(
    tinyobjloader
    PROPERTIES
        INTERFACE_MAPVINA_NAME "tinyobjloader"
        INTERFACE_MAPVINA_URL "https://github.com/tinyobjloader/tinyobjloader.git"
        INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/tinyobjloader/LICENSE
)
