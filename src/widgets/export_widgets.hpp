// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_WIDGETS_EXPORT_H
#define QMAPVINA_WIDGETS_EXPORT_H

#include <QtCore/QtGlobal>

#if !defined(QT_MAPVINA_STATIC)
#if defined(QT_BUILD_MAPVINA_WIDGETS_LIB)
#define Q_MAPVINA_WIDGETS_EXPORT Q_DECL_EXPORT
#else
#define Q_MAPVINA_WIDGETS_EXPORT Q_DECL_IMPORT
#endif
#else
#define Q_MAPVINA_WIDGETS_EXPORT
#endif

#endif // QMAPVINA_WIDGETS_EXPORT_H
