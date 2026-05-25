if(TARGET mbgl-vendor-expected-lite)
	return()
endif()

add_library(
	mbgl-vendor-expected-lite INTERFACE
)

target_include_directories(
	mbgl-vendor-expected-lite SYSTEM
	INTERFACE ${CMAKE_CURRENT_LIST_DIR}/expected-lite/include
)

set_target_properties(
	mbgl-vendor-expected-lite
	PROPERTIES
		INTERFACE_MAPVINA_NAME "expected-lite"
		INTERFACE_MAPVINA_URL "https://github.com/martinmoene/expected-lite"
		INTERFACE_MAPVINA_AUTHOR "Martin Moene"
		INTERFACE_MAPVINA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/expected-lite/LICENSE.txt
)
