// Copyright (C) 2023 MapVina contributors
// Copyright (C) 2019 Mapbox, Inc.

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_UTILS_H
#define QMAPVINA_UTILS_H

#include <QMapVina/Export>
#include <QMapVina/Types>

namespace QMapVina {

enum NetworkMode {
    Online, // Default
    Offline,
};

enum RendererType {
    OpenGL = 3, // same as QSGRendererInterface::OpenGL
    Vulkan = 5, // same as QSGRendererInterface::Vulkan
    Metal = 6,  // same as QSGRendererInterface::Metal
};

Q_MAPVINA_CORE_EXPORT RendererType supportedRendererType();

Q_MAPVINA_CORE_EXPORT NetworkMode networkMode();
Q_MAPVINA_CORE_EXPORT void setNetworkMode(NetworkMode mode);

Q_MAPVINA_CORE_EXPORT double metersPerPixelAtLatitude(double latitude, double zoom);
Q_MAPVINA_CORE_EXPORT ProjectedMeters projectedMetersForCoordinate(const Coordinate &coordinate);
Q_MAPVINA_CORE_EXPORT Coordinate coordinateForProjectedMeters(const ProjectedMeters &projectedMeters);

} // namespace QMapVina

#endif // QMAPVINA_UTILS_H
