// Copyright (C) 2023 MapVina contributors

// SPDX-License-Identifier: BSD-2-Clause

#pragma once

#include <QMapVina/Settings>
#include <QMapVinaWidgets/MapWidget>

#include <QtCore/QPropertyAnimation>

#include <memory>

namespace QMapVina::Test {

class MapWidgetTester : public MapWidget {
    Q_OBJECT

public:
    explicit MapWidgetTester(const QMapVina::Settings &);

    void initializeAnimation();
    int selfTest();

private slots:
    void animationValueChanged();
    void animationFinished() const;

private:
    std::unique_ptr<QPropertyAnimation> m_bearingAnimation;
    std::unique_ptr<QPropertyAnimation> m_zoomAnimation;

    unsigned m_animationTicks{};
    unsigned m_frameDraws{};
};

} // namespace QMapVina::Test
