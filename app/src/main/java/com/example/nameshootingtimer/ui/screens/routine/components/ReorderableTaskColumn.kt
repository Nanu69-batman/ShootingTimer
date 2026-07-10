package com.example.nameshootingtimer.ui.screens.routine.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.nameshootingtimer.domain.model.Task
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

/**
 * ReorderableTaskColumn - Option 1: Native "Ghost" Transformation.
 * 
 * Strategy:
 * - When an item is dragged, its visual appearance in the list REPLACES with the placeholder.
 * - The library's internal movement logic handles the insertion index naturally.
 * - A secondary layer renders the "floating" card content on top of the placeholder.
 */
@Composable
fun ReorderableTaskColumn(
    tasks: List<Task>,
    onDragSettle: (List<Task>) -> Unit,
    taskContent: @Composable (Task, isDragging: Boolean) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()
    
    // Internal state for fluid reordering feedback
    var localList by remember(tasks) { mutableStateOf(tasks) }
    
    val reorderableState = rememberReorderableLazyListState(listState) { from, to ->
        localList = localList.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    // Single-update persistence
    val isAnyItemDragging = reorderableState.isAnyItemDragging
    var wasDragging by remember { mutableStateOf(false) }
    
    LaunchedEffect(isAnyItemDragging) {
        if (!isAnyItemDragging && wasDragging) {
            onDragSettle(localList)
        }
        wasDragging = isAnyItemDragging
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(localList, key = { _, task -> task.id }) { _, task ->
            ReorderableItem(reorderableState, key = task.id) { isDragging ->
                // Animated properties for the floating card
                val liftScale by animateFloatAsState(
                    targetValue = if (isDragging) 1.04f else 1f,
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
                    label = "lift_scale"
                )
                val liftShiftX by animateDpAsState(
                    targetValue = if (isDragging) 8.dp else 0.dp,
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
                    label = "lift_shift"
                )
                val liftElevation by animateDpAsState(
                    targetValue = if (isDragging) 16.dp else 0.dp,
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
                    label = "lift_elevation"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(if (isDragging) 10f else 1f)
                        .longPressDraggableHandle(
                            onDragStarted = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
                        )
                ) {
                    // THE GHOST PLACEHOLDER
                    // This is shown ONLY on the item currently occupying the target index.
                    if (isDragging) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.medium
                                )
                        )
                    }

                    // THE FLOATING CARD CONTENT
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = liftScale
                                scaleY = liftScale
                                translationX = liftShiftX.toPx()
                                // The library applies Y translation to the whole ReorderableItem.
                                // We stay on top of the placeholder at Y=0 relative to the item container.
                            }
                            .shadow(liftElevation, MaterialTheme.shapes.medium, clip = false)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                    ) {
                        taskContent(task, isDragging)
                    }
                }
            }
        }
    }
}
