package com.example.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Luxury motion easing curves for Richlogy sovereign navigation
 */
val LuxuryDecelerateEasing = CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f)
val LuxurySpringEasing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1.0f)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.luxurySharedElement(
    key: String,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
): Modifier {
    if (sharedTransitionScope == null || animatedVisibilityScope == null) return this
    return with(sharedTransitionScope) {
        this@luxurySharedElement.sharedElement(
            state = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = 480, easing = LuxuryDecelerateEasing)
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.luxurySharedBounds(
    key: String,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?
): Modifier {
    if (sharedTransitionScope == null || animatedVisibilityScope == null) return this
    return with(sharedTransitionScope) {
        this@luxurySharedBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = 480, easing = LuxuryDecelerateEasing)
            }
        )
    }
}
