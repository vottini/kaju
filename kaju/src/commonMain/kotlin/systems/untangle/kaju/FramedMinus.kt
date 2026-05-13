package br.mil.marinha.ipqm.c2fn.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

fun buildFramedMinus(
    fill: Color,
    stroke: Color
) =
    ImageVector.Builder(
        name = "FramedMinus",
        defaultWidth = 122.88.dp,
        defaultHeight = 119.8.dp,
        viewportWidth = 122.88f,
        viewportHeight = 119.8f
    ).apply {
        path(
            fill = SolidColor(fill),
            strokeLineWidth = 1.36473f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(10.68f, 10.68f)
            horizontalLineToRelative(98.64f)
            verticalLineToRelative(96.75f)
            horizontalLineToRelative(-98.64f)
            close()
        }
        path(fill = SolidColor(stroke)) {
            moveToRelative(23.59f, 0f)
            horizontalLineToRelative(75.7f)
            curveToRelative(13.02f, 0.02f, 23.57f, 10.57f, 23.59f, 23.59f)
            verticalLineToRelative(72.62f)
            curveToRelative(-0.03f, 13.02f, -10.57f, 23.56f, -23.59f, 23.59f)
            horizontalLineToRelative(-75.7f)
            curveToRelative(-6.26f, -0f, -12.26f, -2.5f, -16.67f, -6.93f)
            lineTo(6.54f, 112.45f)
            curveTo(2.35f, 108.08f, 0.01f, 102.26f, 0f, 96.21f)
            lineTo(0f, 23.59f)
            curveTo(0.02f, 10.57f, 10.57f, 0.02f, 23.59f, 0f)
            close()
            moveTo(83.29f, 53.51f)
            curveToRelative(3.63f, -0.14f, 6.64f, 2.76f, 6.64f, 6.39f)
            curveToRelative(0f, 3.63f, -3.02f, 6.53f, -6.64f, 6.39f)
            horizontalLineToRelative(-43.7f)
            curveToRelative(-3.63f, 0.14f, -6.64f, -2.76f, -6.64f, -6.39f)
            curveToRelative(0f, -3.63f, 3.02f, -6.53f, 6.64f, -6.39f)
            close()
            moveTo(99.29f, 12.77f)
            horizontalLineToRelative(-75.7f)
            curveToRelative(-5.97f, 0.02f, -10.8f, 4.85f, -10.82f, 10.82f)
            verticalLineToRelative(72.62f)
            curveToRelative(-0f, 2.74f, 1.03f, 5.37f, 2.9f, 7.37f)
            lineToRelative(0.28f, 0.26f)
            curveToRelative(2.02f, 2.03f, 4.77f, 3.17f, 7.64f, 3.16f)
            horizontalLineToRelative(75.7f)
            curveToRelative(5.96f, -0.03f, 10.79f, -4.86f, 10.82f, -10.82f)
            lineTo(110.11f, 23.59f)
            curveToRelative(-0.02f, -5.97f, -4.85f, -10.8f, -10.82f, -10.82f)
            close()
        }
    }.build()
