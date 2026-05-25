// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_LOCATION_EXPORT_H
#define QMAPVINA_LOCATION_EXPORT_H

#include <QtCore/QtGlobal>

#if !defined(QT_MAPVINA_STATIC)
#if defined(QT_BUILD_MAPVINA_LOCATION_LIB)
#define Q_MAPVINA_LOCATION_EXPORT Q_DECL_EXPORT
#else
#define Q_MAPVINA_LOCATION_EXPORT Q_DECL_IMPORT
#endif
#else
#define Q_MAPVINA_LOCATION_EXPORT
#endif

#endif // QMAPVINA_LOCATION_EXPORT_H
