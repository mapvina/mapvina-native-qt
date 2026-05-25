// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#ifndef QMAPVINA_MAP_WIDGET_H
#define QMAPVINA_MAP_WIDGET_H

#include <QMapVinaWidgets/Export>

#include <QMapVina/Map>
#include <QMapVina/Settings>
#include <QMapVina/Types>

#include <QtWidgets/QRhiWidget>

#include <memory>

QT_BEGIN_NAMESPACE
class QKeyEvent;
class QMouseEvent;
class QWheelEvent;
QT_END_NAMESPACE

namespace QMapVina {

class MapWidgetPrivate;

class Q_MAPVINA_WIDGETS_EXPORT MapWidget : public QRhiWidget {
    Q_OBJECT

public:
    explicit MapWidget(const Settings &settings);
    ~MapWidget() override;

    [[nodiscard]] Map *map();

signals:
    void onMouseDoubleClickEvent(QMapVina::Coordinate coordinate);
    void onMouseMoveEvent(QMapVina::Coordinate coordinate);
    void onMousePressEvent(QMapVina::Coordinate coordinate);
    void onMouseReleaseEvent(QMapVina::Coordinate coordinate);

public slots:
    void handleMapChange(QMapVina::Map::MapChange change);

protected:
    // Event handlers
    void mousePressEvent(QMouseEvent *event) override;
    void mouseReleaseEvent(QMouseEvent *event) override;
    void mouseMoveEvent(QMouseEvent *event) override;
    void wheelEvent(QWheelEvent *event) override;
    void resizeEvent(QResizeEvent *event) override;
    bool event(QEvent *e) override;

    // QRhiWidget implementation
    void initialize(QRhiCommandBuffer *cb) override;
    void render(QRhiCommandBuffer *cb) override;
    void releaseResources() override;

private:
    Q_DISABLE_COPY(MapWidget);

    std::unique_ptr<MapWidgetPrivate> d_ptr;
};

} // namespace QMapVina

#endif // QMAPVINA_MAP_WIDGET_H
