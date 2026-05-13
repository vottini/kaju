package systems.untangle.kaju.preview

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import systems.untangle.kaju.Kaju
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
    }
}
