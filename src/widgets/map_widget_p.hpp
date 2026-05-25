// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_MAP_WIDGET_P_H
#define QMAPVINA_MAP_WIDGET_P_H

#include <QMapVina/Map>
#include <QMapVina/Settings>

#include <memory>

QT_BEGIN_NAMESPACE
class QKeyEvent;
class QMouseEvent;
class QWheelEvent;
QT_END_NAMESPACE

namespace QMapVina {

class MapWidgetPrivate : public QObject {
    Q_OBJECT

public:
    explicit MapWidgetPrivate(QObject *parent, Settings settings);
    ~MapWidgetPrivate() override;

    void handleMousePressEvent(QMouseEvent *event);
    void handleMouseMoveEvent(QMouseEvent *event);
    void handleWheelEvent(QWheelEvent *event) const;

    std::unique_ptr<Map> m_map;
    Settings m_settings;
    bool m_initialized{};

private:
    Q_DISABLE_COPY(MapWidgetPrivate);

    QPointF m_lastPos;
};

} // namespace QMapVina

#endif // QMAPVINA_MAP_WIDGET_P_H
