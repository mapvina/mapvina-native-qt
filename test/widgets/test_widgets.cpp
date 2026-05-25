// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#include "main_window.hpp"
#include "map_widget_tester.hpp"
#include "map_window.hpp"

#include <QDebug>
#include <QTest>

#include <memory>

class TestWidgets : public QObject {
    Q_OBJECT

private slots:
    void testGLWidgetNoProvider();
    void testGLWidgetMapVinaProvider();
    void testGLWidgetDocking();
    void testGLWidgetStyle();
};

void TestWidgets::testGLWidgetNoProvider() {
    const QMapVina::Settings settings;
    auto widget = std::make_unique<QMapVina::MapWidget>(settings);
    widget->show();
    QTest::qWait(1000);
}

void TestWidgets::testGLWidgetMapVinaProvider() {
    QMapVina::Settings settings(QMapVina::Settings::MapVinaProvider);
    settings.setDefaultCoordinate(QMapVina::Coordinate(59.91, 10.75));
    settings.setDefaultZoom(4);
    auto tester = std::make_unique<QMapVina::Test::MapWidgetTester>(settings);
    tester->show();
    QTest::qWait(1000);
    qDebug() << "Resizing to" << QSize(800, 600);
    tester->resize(800, 600);
    QTest::qWait(1000);
    qDebug() << "Resizing to" << QSize(400, 300);
    tester->resize(400, 300);
    QTest::qWait(1000);
    qDebug() << "Hiding";
    tester->hide();
    QTest::qWait(500);
    qDebug() << "Showing";
    tester->show();
    QTest::qWait(1000);
}

void TestWidgets::testGLWidgetDocking() {
    QMapVina::Settings settings(QMapVina::Settings::MapVinaProvider);
    settings.setDefaultCoordinate(QMapVina::Coordinate(59.91, 10.75));
    settings.setDefaultZoom(4);
    auto tester = std::make_unique<QMapVina::Test::MainWindow>();
    tester->show();
    QTest::qWait(1000);
    qDebug() << "Undocking";
    QMapVina::Test::MapWindow *window = tester->currentCentralWidget();
    window->dockUndock();
    QTest::qWait(500);
    qDebug() << "Resizing undocked window to" << QSize(800, 600);
    window->resize(800, 600);
    QTest::qWait(500);
    qDebug() << "Docking back";
    window->dockUndock();
    QTest::qWait(1000);
}

void TestWidgets::testGLWidgetStyle() {
    QMapVina::Styles styles;
    styles.append(QMapVina::Style("https://maps.mapvina.com/styles/v2/streets.json?key=public_key", "Demo tiles"));

    QMapVina::Settings settings;
    settings.setStyles(styles);
    auto tester = std::make_unique<QMapVina::Test::MapWidgetTester>(settings);
    tester->show();
    QTest::qWait(100);
    tester->initializeAnimation();
    QTest::qWait(tester->selfTest());
}

// NOLINTNEXTLINE(misc-const-correctness)
QTEST_MAIN(TestWidgets)
#include "test_widgets.moc"
