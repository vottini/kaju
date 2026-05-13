package br.mil.marinha.ipqm.c2fn.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

fun buildFramedPlus(
    fill: Color,
    stroke: Color
) =
    ImageVector.Builder(
        name = "FramedPlus",
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
            moveTo(23.59f, 0f)
            horizontalLineToRelative(75.7f)
            arcToRelative(23.63f, 23.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23.59f, 23.59f)
            verticalLineTo(96.21f)
            arcTo(23.64f, 23.64f, 0f, isMoreThanHalf = false, isPositiveArc = true, 99.29f, 119.8f)
            horizontalLineTo(23.59f)
            arcToRelative(23.53f, 23.53f, 0f, isMoreThanHalf = false, isPositiveArc = true, -16.67f, -6.93f)
            lineToRelative(-0.38f, -0.42f)
            arcTo(23.49f, 23.49f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 96.21f)
            verticalLineTo(23.59f)
            arcTo(23.63f, 23.63f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23.59f, 0f)
            close()
            moveTo(55.06f, 38.05f)
            arcToRelative(6.38f, 6.38f, 0f, isMoreThanHalf = true, isPositiveArc = true, 12.76f, 0f)
            verticalLineTo(53.51f)
            horizontalLineTo(83.29f)
            arcToRelative(6.39f, 6.39f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 12.77f)
            horizontalLineTo(67.82f)
            verticalLineTo(81.75f)
            arcToRelative(6.38f, 6.38f, 0f, isMoreThanHalf = false, isPositiveArc = true, -12.76f, 0f)
            verticalLineTo(66.28f)
            horizontalLineTo(39.59f)
            arcToRelative(6.39f, 6.39f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -12.77f)
            horizontalLineTo(55.06f)
            verticalLineTo(38.05f)
            close()
            moveTo(99.29f, 12.77f)
            horizontalLineTo(23.59f)
            arcTo(10.86f, 10.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12.77f, 23.59f)
            verticalLineTo(96.21f)
            arcToRelative(10.77f, 10.77f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2.9f, 7.37f)
            lineToRelative(0.28f, 0.26f)
            arcTo(10.76f, 10.76f, 0f, isMoreThanHalf = false, isPositiveArc = false, 23.59f, 107f)
            horizontalLineToRelative(75.7f)
            arcToRelative(10.87f, 10.87f, 0f, isMoreThanHalf = false, isPositiveArc = false, 10.82f, -10.82f)
            verticalLineTo(23.59f)
            arcTo(10.86f, 10.86f, 0f, isMoreThanHalf = false, isPositiveArc = false, 99.29f, 12.77f)
            close()
        }
    }.build()
