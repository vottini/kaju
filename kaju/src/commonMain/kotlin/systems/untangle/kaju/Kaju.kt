package systems.untangle.kaju

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.mil.marinha.ipqm.c2fn.icons.buildFramedMinus
import br.mil.marinha.ipqm.c2fn.icons.buildFramedPlus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class KajuIcons(
    val collapsed: ImageVector,
    val expanded: ImageVector
)

val DefaultKajuIcons = KajuIcons(
    buildFramedPlus(Color.Companion.White, Color.Companion.Black),
    buildFramedMinus(Color.Companion.White, Color.Companion.Black)
)

data class KajuConfig (
    val indentation: Dp,
    val itemsGap: Dp,
    val itemLeftPadding: Dp,
    val showLines: Boolean,
    val icons: KajuIcons
)

val DefaultKajuConfig = KajuConfig(
    indentation = 10.dp,
    itemsGap = 10.dp,
    itemLeftPadding = 10.dp,
    showLines = true,
    icons = DefaultKajuIcons
)

class KajuState <T> {
    private val _expandedIds: MutableStateFlow<Set<T>> = MutableStateFlow(emptySet())
    val expandedIds: StateFlow<Set<T>> = _expandedIds

    fun toggleExpansion(id: T) {
        _expandedIds.update { current ->
            if (current.contains(id)) current - id
            else current + id
        }
    }
}

@Composable
fun <T> rememberKajuState(): KajuState <T> {
    val state: KajuState <T> = remember { KajuState() }
    return state
}

enum class LineSegment {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

@Composable
fun Drawer(
    lines: List <LineSegment>,
    parentHeight: Dp,
    extension: Dp,
    toggler: (() -> Unit)?
) {
    val totalLength = 20f + extension.value
    val halfLength = totalLength / 2f

    val baseModifier = when {
        toggler != null -> Modifier.Companion.clickable(onClick = toggler)
        else -> Modifier.Companion
    }

    Canvas(
        modifier = baseModifier.then(
            Modifier.Companion
                .height(parentHeight)
                .width(20.dp)
        )
    ) {
        for (line in lines) {
            when (line) {
                LineSegment.TOP -> drawLine(
                    color = Color.Companion.Black,
                    start = Offset(10f, 0f),
                    end = Offset(10f, halfLength)
                )

                LineSegment.RIGHT -> drawLine(
                    color = Color.Companion.Black,
                    start = Offset(10f, halfLength),
                    end = Offset(20f, halfLength)
                )

                LineSegment.BOTTOM -> drawLine(
                    color = Color.Companion.Black,
                    start = Offset(10f, halfLength),
                    end = Offset(10f, totalLength)
                )

                LineSegment.LEFT -> drawLine(
                    color = Color.Companion.Black,
                    start = Offset(10f, halfLength),
                    end = Offset(0f, halfLength)
                )
            }
        }
    }
}

@Composable
fun Filling(
    lines: List <LineSegment>,
    parentHeight: Dp,
    extension: Dp
) {
    Drawer(
        lines,
        parentHeight,
        extension,
        null
    )
}

@Composable
fun Node(
    lines: List <LineSegment>,
    parentHeight: Dp,
    extension: Dp,
    toggler: () -> Unit
) {
    Drawer(
        lines,
        parentHeight,
        extension,
        toggler
    )
}

val expanded_node = listOf (LineSegment.LEFT, LineSegment.BOTTOM)
val collapsed_node = listOf (LineSegment.LEFT)

val empty_filling = listOf <LineSegment> ()
val through_filling = listOf (LineSegment.TOP, LineSegment.BOTTOM)
val item_filling = listOf (LineSegment.TOP, LineSegment.BOTTOM, LineSegment.RIGHT)
val tail_filling = listOf (LineSegment.TOP, LineSegment.RIGHT)

enum class FillingType {
    EMPTY,
    THROUGH,
    TAIL
}

private fun <T, U> LazyListScope.renderNode(
    element: T,
    leavesRetriever: (T) -> Collection<T>,
    identifier: (T) -> U,
    expandedIds: Set<U>,
    toggleElement: (U) -> Unit,
    preamble: MutableList <FillingType>,
    config: KajuConfig,
    renderer: @Composable (T) -> Unit
) {
    val ownId = identifier(element)
    val children = leavesRetriever(element)
    val isLeaf = children.isEmpty()

    val isExpanded = !isLeaf && expandedIds.contains(ownId)
    val nodeLines = if (isExpanded) expanded_node else collapsed_node

    item(ownId) {
        var parentHeightPx by remember { mutableStateOf(0) }
        val density = LocalDensity.current
        val parentHeightDp = with(density) { parentHeightPx.toDp() }

        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.onSizeChanged { parentHeightPx = it.height }
        ) {
            if (preamble.isNotEmpty()) {
                val lastIndex = preamble.size - 1

                Row {
                    preamble.forEachIndexed { index, filled ->
                        when (filled) {
                            FillingType.THROUGH -> {
                                val segments = when (index != lastIndex) {
                                    true -> through_filling
                                    false -> item_filling
                                }

                                Filling(
                                    segments,
                                    parentHeightDp,
                                    config.itemsGap
                                )
                            }

                            FillingType.EMPTY -> Filling(
                                empty_filling,
                                parentHeightDp,
                                config.itemsGap
                            )

                            FillingType.TAIL -> Filling(
                                tail_filling,
                                parentHeightDp,
                                config.itemsGap
                            )
                        }
                    }
                }
            }

            if (isLeaf) {
                Filling(
                    nodeLines,
                    parentHeightDp,
                    config.itemsGap
                )
            }

            else {
                val icon = when {
                    isExpanded -> config.icons.expanded
                    else -> config.icons.collapsed
                }

                Box(contentAlignment = Alignment.Companion.Center) {
                    Node(
                        nodeLines,
                        parentHeightDp,
                        config.itemsGap
                    ) {
                        toggleElement(ownId)
                    }

                    Icon(
                        imageVector = icon,
                        tint = Color.Companion.Unspecified,
                        modifier = Modifier.Companion.size(15.dp),
                        contentDescription = ""
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.Companion.padding(
                    top = config.itemsGap / 2,
                    bottom = config.itemsGap / 2,
                    start = config.itemLeftPadding
                )
            ) {
                renderer(element)
            }
        }
    }

    if (isExpanded) {
        val lastElement = children.last()

        children.forEach { child ->
            val childPreamble = preamble
                .map { filling -> if (filling == FillingType.TAIL) FillingType.EMPTY else filling }
                .toMutableList()

            childPreamble.add(
                if (child === lastElement) FillingType.TAIL
                else FillingType.THROUGH
            )

            renderNode(
                child,
                leavesRetriever,
                identifier,
                expandedIds,
                toggleElement,
                childPreamble,
                config,
                renderer
            )
        }
    }
}

@Composable
fun <H, T, U> Kaju(
    header: H,
    rootSelector: (H) -> T?,
    leavesRetriever: (T) -> Collection<T>,
    identifier: (T) -> U,
    treeState: KajuState<U>,
    config: KajuConfig = DefaultKajuConfig,
    onSelect: (T) -> Unit = {},
    modifier: Modifier = Modifier.Companion,
    renderer: @Composable ((T) -> Unit)
) {
    val rootElement = rootSelector(header)
    if (null == rootElement) {
        return
    }

    val expandedIds = treeState.expandedIds.collectAsState()
    val toggleElement = { elementId: U -> treeState.toggleExpansion(elementId) }
    val preamble = mutableListOf<FillingType>()

    LazyColumn(
        modifier = modifier
    ) {
        renderNode(
            rootElement,
            leavesRetriever,
            identifier,
            expandedIds.value,
            toggleElement,
            preamble,
            config,
            renderer
        )
    }
}