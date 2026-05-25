// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: MIT

#ifndef WINDOW_H
#define WINDOW_H

#include <QMapVinaWidgets/MapWidget>

#include <QPushButton>
#include <QVBoxLayout>
#include <QWidget>

#include <memory>

class MainWindow;

class Window : public QWidget {
    Q_OBJECT

public:
    explicit Window(MainWindow *mainWindow);

protected:
    void keyPressEvent(QKeyEvent *event) override;

private slots:
    void dockUndock();

private:
    QMapVina::MapWidget *m_mapWidget{}; // Qt will manage lifetime via parent-child
    std::unique_ptr<QVBoxLayout> m_layout;
    std::unique_ptr<QPushButton> m_buttonDock;
    MainWindow *m_mainWindowRef{};
};

#endif
