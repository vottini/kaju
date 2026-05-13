package systems.untangle.kaju

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
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
    buildFramedPlus(Color.White, Color.Black),
    buildFramedMinus(Color.White, Color.Black)
)

data class KajuConfig(
    val indentation: Dp,
    val itemsGap: Dp,
    val itemLeftPadding: Dp,
    val showLines: Boolean,
    val lineColor: Color,
    val icons: KajuIcons
)

val DefaultKajuConfig = KajuConfig(
    indentation = 10.dp,
    itemsGap = 10.dp,
    itemLeftPadding = 10.dp,
    showLines = true,
    lineColor = Color.Black,
    icons = DefaultKajuIcons
)

class KajuState<T> {
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
fun <T> rememberKajuState(): KajuState<T> = remember { KajuState() }

enum class LineSegment {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT
}

val expanded_node  = listOf(LineSegment.LEFT, LineSegment.BOTTOM)
val collapsed_node = listOf(LineSegment.LEFT)

val empty_filling   = listOf<LineSegment>()
val through_filling = listOf(LineSegment.TOP, LineSegment.BOTTOM)
val item_filling    = listOf(LineSegment.TOP, LineSegment.BOTTOM, LineSegment.RIGHT)
val tail_filling    = listOf(LineSegment.TOP, LineSegment.RIGHT)

enum class FillingType { EMPTY, THROUGH, TAIL }

private fun DrawScope.drawSegments(lines: List<LineSegment>, color: Color) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    for (line in lines) {
        when (line) {
            LineSegment.TOP    -> drawLine(color, Offset(cx, 0f), Offset(cx, cy))
            LineSegment.BOTTOM -> drawLine(color, Offset(cx, cy), Offset(cx, size.height))
            LineSegment.RIGHT  -> drawLine(color, Offset(cx, cy), Offset(size.width, cy))
            LineSegment.LEFT   -> drawLine(color, Offset(cx, cy), Offset(0f, cy))
        }
    }
}

private fun Modifier.connectors(lines: List<LineSegment>, color: Color): Modifier =
    fillMaxHeight().width(20.dp).drawBehind { drawSegments(lines, color) }

private fun <T, U> LazyListScope.renderNode(
    element: T,
    leavesRetriever: (T) -> Collection<T>,
    identifier: (T) -> U,
    expandedIds: Set<U>,
    toggleElement: (U) -> Unit,
    preamble: MutableList<FillingType>,
    config: KajuConfig,
    collapsible: Boolean,
    renderer: @Composable (T) -> Unit
) {
    val ownId = identifier(element)
    val children = leavesRetriever(element)
    val isLeaf = children.isEmpty()
    val isExpanded = !isLeaf && (if (collapsible) expandedIds.contains(ownId) else true)
    val nodeLines = if (isExpanded) expanded_node else collapsed_node

    item(ownId) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Max)
        ) {
            if (preamble.isNotEmpty()) {
                val lastIndex = preamble.size - 1
                Row(modifier = Modifier.fillMaxHeight()) {
                    preamble.forEachIndexed { index, filling ->
                        val segments = when (filling) {
                            FillingType.THROUGH -> if (index != lastIndex) through_filling else item_filling
                            FillingType.EMPTY   -> empty_filling
                            FillingType.TAIL    -> tail_filling
                        }
                        Spacer(Modifier.connectors(segments, config.lineColor))
                    }
                }
            }

            if (isLeaf || !collapsible) {
                Spacer(Modifier.connectors(nodeLines, config.lineColor))
            } else {
                val icon = if (isExpanded) config.icons.expanded else config.icons.collapsed
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxHeight().width(20.dp)
                ) {
                    Spacer(
                        Modifier
                            .connectors(nodeLines, config.lineColor)
                            .clickable { toggleElement(ownId) }
                    )
                    Icon(
                        imageVector = icon,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(15.dp),
                        contentDescription = ""
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(
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
                .map { if (it == FillingType.TAIL) FillingType.EMPTY else it }
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
                collapsible,
                renderer
            )
        }
    }
}

/**
 * Displays a lazily-rendered, collapsible tree view.
 *
 * The tree is described indirectly through a [header] object, allowing the root node to be
 * absent (e.g. while loading). Connector lines between nodes are drawn via `Modifier.drawBehind`
 * so they resolve in the same layout pass without recomposition flicker.
 *
 * @param H Type of the header object that owns the tree root.
 * @param T Type of each tree node.
 * @param U Type of the unique identifier for each node. Must be usable as a `LazyColumn` item key.
 * @param header The object from which the root node is derived.
 * @param rootSelector Extracts the root node from [header]. Return `null` to render nothing.
 * @param leavesRetriever Returns the direct children of a given node. Return an empty collection for leaf nodes.
 * @param identifier Returns a stable, unique key for a node. Used both for expansion state and lazy list keys.
 * @param treeState Holds the set of currently expanded node ids. Create with [rememberKajuState].
 * @param config Visual configuration (indentation, gap, line color, icons). Defaults to [DefaultKajuConfig].
 * @param collapsible When `false`, all nodes are permanently expanded and toggle controls are hidden.
 * @param modifier [Modifier] applied to the underlying [LazyColumn].
 * @param renderer Composable content rendered for each node, receiving the node value.
 */
@Composable
fun <H, T, U> Kaju(
    header: H,
    rootSelector: (H) -> T?,
    leavesRetriever: (T) -> Collection<T>,
    identifier: (T) -> U,
    treeState: KajuState<U>,
    config: KajuConfig = DefaultKajuConfig,
    collapsible: Boolean = true,
    modifier: Modifier = Modifier,
    renderer: @Composable (T) -> Unit
) {
    val rootElement = rootSelector(header) ?: return
    val expandedIds = treeState.expandedIds.collectAsState()
    val toggleElement = { id: U -> treeState.toggleExpansion(id) }

    LazyColumn(modifier = modifier) {
        renderNode(
            rootElement, leavesRetriever, identifier,
            expandedIds.value, toggleElement,
            mutableListOf(), config, collapsible, renderer
        )
    }
}
