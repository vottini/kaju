# Kaju

A Kotlin Multiplatform Compose library for rendering collapsible tree views, targeting Android and JVM (desktop).

## Features

- Lazily rendered via `LazyColumn` — handles large trees efficiently
- Connector lines drawn with `Modifier.drawBehind`, resolving in a single layout pass with no recomposition flicker
- Optional non-collapsible mode to display the full tree statically
- Configurable indentation, gap, line color, and expand/collapse icons

## Setup

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("systems.untangle:kaju:0.1.0")
}
```

## Usage

### Basic collapsible tree

```kotlin
data class FileNode(val id: Int, val name: String, val children: List<FileNode> = emptyList())

val root = FileNode(
    id = 0, name = "root", children = listOf(
        FileNode(id = 1, name = "src", children = listOf(
            FileNode(id = 2, name = "Main.kt"),
            FileNode(id = 3, name = "Utils.kt"),
        )),
        FileNode(id = 4, name = "build.gradle.kts"),
    )
)

@Composable
fun MyTree() {
    val state = rememberKajuState<Int>()

    Kaju(
        header = root,
        rootSelector = { it },
        leavesRetriever = { it.children },
        identifier = { it.id },
        treeState = state,
    ) { node ->
        Text(node.name)
    }
}
```

### Always expanded (non-collapsible)

```kotlin
Kaju(
    header = root,
    rootSelector = { it },
    leavesRetriever = { it.children },
    identifier = { it.id },
    treeState = rememberKajuState(),
    collapsible = false,
) { node ->
    Text(node.name)
}
```

### Custom configuration

```kotlin
val config = KajuConfig(
    indentation = 16.dp,
    itemsGap = 8.dp,
    itemLeftPadding = 12.dp,
    showLines = true,
    lineColor = Color.Gray,
    icons = KajuIcons(
        collapsed = myCollapsedIcon,
        expanded = myExpandedIcon,
    )
)

Kaju(
    header = root,
    rootSelector = { it },
    leavesRetriever = { it.children },
    identifier = { it.id },
    treeState = rememberKajuState(),
    config = config,
) { node ->
    Text(node.name)
}
```

## API

| Symbol | Description |
|---|---|
| `Kaju` | Main composable. Renders the tree inside a `LazyColumn`. |
| `KajuState<T>` | Holds the set of expanded node ids. |
| `rememberKajuState<T>()` | Creates and remembers a `KajuState` across recompositions. |
| `KajuConfig` | Visual configuration for the tree. |
| `DefaultKajuConfig` | Default configuration values. |
| `KajuIcons` | Pair of `ImageVector` for collapsed and expanded states. |

## License

MIT
