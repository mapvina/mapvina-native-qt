// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#pragma once

#include <QMapVinaWidgets/MapWidget>

#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QWidget>

#include <memory>

namespace QMapVina::Test {

class MainWindow;

class MapWindow : public QWidget {
    Q_OBJECT

public:
    explicit MapWindow(MainWindow *mainWindow);

public slots:
    void dockUndock();

private:
    QMapVina::MapWidget *m_mapWidget{};
    std::unique_ptr<QVBoxLayout> m_layout;
    MainWindow *m_mainWindowRef{};
};

} // namespace QMapVina::Test
