// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#pragma once

#include "map_window.hpp"

#include <QtWidgets/QMainWindow>

namespace QMapVina::Test {

class MainWindow : public QMainWindow {
    Q_OBJECT

public:
    explicit MainWindow();

    void addNewWindow();
    [[nodiscard]] MapWindow *currentCentralWidget() const { return static_cast<MapWindow *>(centralWidget()); }
};

} // namespace QMapVina::Test
