package systems.untangle.kaju.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import systems.untangle.kaju.DefaultKajuConfig
import systems.untangle.kaju.Kaju
import systems.untangle.kaju.KajuConfig
import systems.untangle.kaju.rememberKajuState

private data class Node(val id: Int, val label: String, val children: List<Node> = emptyList())

private val previewTree = Node(
    id = 0, label = "Root", children = listOf(
        Node(
            id = 1, label = "Parent A", children = listOf(
                Node(id = 2, label = "Child A1"),
                Node(id = 3, label = "Child A2"),
            )
        ),
        Node(
            id = 4, label = "Parent B", children = listOf(
                Node(id = 5, label = "Child B1"),
            )
        ),
        Node(id = 6, label = "Leaf"),
    )
)

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Kaju Preview") {
        val state = rememberKajuState<Int>()
        MaterialTheme {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(20.dp)
            ) {
                Column (
                    modifier = Modifier.weight(1f)
                ) {
                    Kaju(
                        header = previewTree,
                        rootSelector = { it },
                        leavesRetriever = { it.children },
                        identifier = { it.id },
                        treeState = state,
                    ) { node ->
                        Text(text = node.label)
                    }
                }

                Column (
                    modifier = Modifier.weight(1f)
                ) {
                    Kaju(
                        header = previewTree,
                        rootSelector = { it },
                        leavesRetriever = { it.children },
                        identifier = { it.id },
                        collapsible = false,
                        treeState = state,
                    ) { node ->
                        Text(text = node.label)
                    }
                }

                Column (
                    modifier = Modifier.weight(1f)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            state.expandAll(previewTree, { it }, { it.children }, { it.id })
                        }) { Text("Expand All") }
                        Button(onClick = { state.collapseAll() }) { Text("Collapse All") }
                    }
                    Kaju(
                        header = previewTree,
                        rootSelector = { it },
                        leavesRetriever = { it.children },
                        identifier = { it.id },
                        config = DefaultKajuConfig.copy(lineColor = Color.Blue),
                        treeState = state,
                    ) { node ->
                        Text(text = node.label)
                    }
                }
            }
        }
    }
}
