// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_CORE_EXPORT_H
#define QMAPVINA_CORE_EXPORT_H

#include <QtCore/QtGlobal>

#if !defined(QT_MAPVINA_STATIC)
#if defined(QT_BUILD_MAPVINA_CORE_LIB)
#define Q_MAPVINA_CORE_EXPORT Q_DECL_EXPORT
#else
#define Q_MAPVINA_CORE_EXPORT Q_DECL_IMPORT
#endif
#else
#define Q_MAPVINA_CORE_EXPORT
#endif

#endif // QMAPVINA_CORE_EXPORT_H
