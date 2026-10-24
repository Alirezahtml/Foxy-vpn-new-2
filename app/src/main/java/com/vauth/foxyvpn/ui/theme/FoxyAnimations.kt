package com.vauth.foxyvpn.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vauth.foxyvpn.data.DynamicIslandLedStyle
import com.vauth.foxyvpn.data.DynamicIslandMotionStyle
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.mutableFloatStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 1. Staggered Entrance Modifier (Fade + Slide Up + Subtle Scale)
 */
fun Modifier.staggeredEntrance(
    index: Int,
    delayPerItemMs: Int = 55,
    durationMs: Int = 420,
): Modifier = composed {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay((index.coerceAtLeast(0) * delayPerItemMs).toLong())
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = durationMs, easing = FastOutSlowInEasing),
        )
    }
    this.graphicsLayer {
        alpha = anim.value
        translationY = (1f - anim.value) * 32f
        val s = 0.96f + (0.04f * anim.value)
        scaleX = s
        scaleY = s
    }
}

/**
 * 2. Tactile Neumorphic Press-Bounce Modifier
 */
fun Modifier.neumorphicTouchBounce(
    pressedScale: Float = 0.965f,
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "touch-bounce-scale",
    )
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(Unit) {
            while (true) {
                awaitPointerEventScope {
                    awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    waitForUpOrCancellation()
                    isPressed = false
                }
            }
        }
}

/**
 * 3. Shake-on-Error Horizontal Keyframe Modifier
 */
fun Modifier.shakeOnTrigger(triggerKey: Any?): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(triggerKey) {
        if (triggerKey != null) {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 460
                    0f at 0
                    (-18f) at 60
                    18f at 120
                    (-14f) at 180
                    14f at 240
                    (-8f) at 310
                    8f at 380
                    0f at 460
                },
            )
        }
    }
    this.graphicsLayer {
        translationX = offsetX.value
    }
}

/**
 * 4. Floating / Levitating Idle Animation Modifier
 */
fun Modifier.subtleFloatAnimation(
    amplitudePx: Float = 8f,
    durationMs: Int = 2200,
): Modifier = composed {
    val infinite = rememberInfiniteTransition(label = "float-idle")
    val dy by infinite.animateFloat(
        initialValue = -amplitudePx,
        targetValue = amplitudePx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMs, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "float-dy",
    )
    this.graphicsLayer {
        translationY = dy
    }
}

/**
 * 4b. Automotive Sequential Chasing RGB LED Border Modifier (14 Ultra-Smooth 120Hz Styles)
 * Uses continuous frame-delta integration so changing speed or style never stutters or resets.
 */
fun Modifier.automotiveLedBorder(
    enabled: Boolean = true,
    style: DynamicIslandLedStyle = DynamicIslandLedStyle.CYBER_RAINBOW,
    speedMultiplier: Float = 1.0f,
    cornerRadius: Dp = 28.dp,
    borderWidth: Dp = 2.4.dp,
    glowRadius: Dp = 8.dp,
): Modifier = composed {
    if (!enabled) return@composed this

    var angleDeg by remember { mutableFloatStateOf(0f) }
    var pulsePhase by remember { mutableFloatStateOf(0f) }
    val clampedSpeed = speedMultiplier.coerceIn(0.3f, 2.5f)

    LaunchedEffect(clampedSpeed) {
        var lastFrameMs = withInfiniteAnimationFrameMillis { it }
        while (isActive) {
            val nowMs = withInfiniteAnimationFrameMillis { it }
            val dtSec = ((nowMs - lastFrameMs).coerceIn(1L, 64L)) / 1000f
            lastFrameMs = nowMs
            // 150 deg/sec at 1.0x -> buttery-smooth 60/120Hz continuous rotation
            angleDeg = (angleDeg + dtSec * 150f * clampedSpeed) % 360f
            pulsePhase = (pulsePhase + dtSec * 3.2f) % (2f * PI.toFloat())
        }
    }

    val pulseGlow = 0.80f + 0.20f * sin(pulsePhase)

    val palette = remember(style) {
        when (style) {
            DynamicIslandLedStyle.CYBER_RAINBOW -> listOf(
                Color(0xFF00F5D4),
                Color(0xFF00BBF9),
                Color(0xFF7B2CBF),
                Color(0xFFFF007F),
                Color(0xFFFF6D00),
                Color(0xFFFFEA00),
                Color(0xFF00F5D4),
            )
            DynamicIslandLedStyle.FOXY_FIRE -> listOf(
                Color(0xFFFF3D00),
                Color(0xFFFF6D00),
                Color(0xFFFFAB00),
                Color(0xFFFFEA00),
                Color(0xFFFF1744),
                Color(0xFFFF3D00),
            )
            DynamicIslandLedStyle.EMERALD_PULSE -> listOf(
                Color(0xFF00E676),
                Color(0xFF00BFA5),
                Color(0xFF18FFFF),
                Color(0xFF10B981),
                Color(0xFF69F0AE),
                Color(0xFF00E676),
            )
            DynamicIslandLedStyle.POLICE_STROBE -> listOf(
                Color(0xFFFF1744),
                Color(0xFFFF5252),
                Color(0xFFFFFFFF),
                Color(0xFF2979FF),
                Color(0xFF00E5FF),
                Color(0xFFFF1744),
            )
            DynamicIslandLedStyle.SOLAR_GOLD -> listOf(
                Color(0xFFFFD700),
                Color(0xFFFFA000),
                Color(0xFFFFF59D),
                Color(0xFFFF6F00),
                Color(0xFFFFD700),
            )
            DynamicIslandLedStyle.AURORA_BOREALIS -> listOf(
                Color(0xFF00F5A0),
                Color(0xFF00D9F5),
                Color(0xFF6366F1),
                Color(0xFFA855F7),
                Color(0xFF10B981),
                Color(0xFF00F5A0),
            )
            DynamicIslandLedStyle.OCEAN_BIOLUME -> listOf(
                Color(0xFF00E5FF),
                Color(0xFF0284C7),
                Color(0xFF38BDF8),
                Color(0xFF67E8F9),
                Color(0xFF1D4ED8),
                Color(0xFF00E5FF),
            )
            DynamicIslandLedStyle.ROYAL_NEBULA -> listOf(
                Color(0xFFC084FC),
                Color(0xFF7C3AED),
                Color(0xFFEC4899),
                Color(0xFF6366F1),
                Color(0xFFF472B6),
                Color(0xFFC084FC),
            )
            DynamicIslandLedStyle.CYBERPUNK_SYNTH -> listOf(
                Color(0xFFFF007F),
                Color(0xFF00F0FF),
                Color(0xFFFCEE09),
                Color(0xFF7928CA),
                Color(0xFFFF007F),
            )
            DynamicIslandLedStyle.LIQUID_MERCURY -> listOf(
                Color(0xFFFFFFFF),
                Color(0xFF94A3B8),
                Color(0xFFE2E8F0),
                Color(0xFF38BDF8),
                Color(0xFFCBD5E1),
                Color(0xFFFFFFFF),
            )
            DynamicIslandLedStyle.ARCTIC_FROST -> listOf(
                Color(0xFFE0F2FE),
                Color(0xFF7DD3FC),
                Color(0xFF38BDF8),
                Color(0xFFBAE6FD),
                Color(0xFF0284C7),
                Color(0xFFE0F2FE),
            )
            DynamicIslandLedStyle.SUNSET_MIAMI -> listOf(
                Color(0xFFFF5E62),
                Color(0xFFFF9966),
                Color(0xFFF43F5E),
                Color(0xFFA855F7),
                Color(0xFFFB7185),
                Color(0xFFFF5E62),
            )
            DynamicIslandLedStyle.QUANTUM_PLASMA -> listOf(
                Color(0xFF22D3EE),
                Color(0xFF818CF8),
                Color(0xFFF43F5E),
                Color(0xFF10B981),
                Color(0xFF22D3EE),
            )
            DynamicIslandLedStyle.SAKURA_BLOSSOM -> listOf(
                Color(0xFFFFB7C5),
                Color(0xFFF472B6),
                Color(0xFFFBCFE8),
                Color(0xFFEC4899),
                Color(0xFFFDA4AF),
                Color(0xFFFFB7C5),
            )
        }
    }

    this.drawWithContent {
        drawContent()
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@drawWithContent

        val strokePx = borderWidth.toPx()
        val cornerPx = cornerRadius.toPx()
        val glowPx = glowRadius.toPx() * pulseGlow
        val center = Offset(w / 2f, h / 2f)

        val shader = SweepGradientShader(
            center = center,
            colors = palette,
            colorStops = null,
        )
        val matrix = android.graphics.Matrix().apply {
            postRotate(angleDeg, center.x, center.y)
        }
        shader.setLocalMatrix(matrix)

        // Outer neon halo glow (automotive ambient light diffuser)
        drawIntoCanvas { canvas ->
            val glowPaint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                this.style = android.graphics.Paint.Style.STROKE
                strokeWidth = strokePx * 1.9f
                this.shader = shader
                alpha = (185 * pulseGlow).toInt().coerceIn(85, 235)
                maskFilter = BlurMaskFilter(glowPx.coerceAtLeast(1f), BlurMaskFilter.Blur.NORMAL)
            }
            val inset = strokePx / 2f
            canvas.nativeCanvas.drawRoundRect(
                inset,
                inset,
                w - inset,
                h - inset,
                cornerPx,
                cornerPx,
                glowPaint,
            )
        }

        // Crisp high-brightness LED core tube
        drawRoundRect(
            brush = ShaderBrush(shader),
            topLeft = Offset(strokePx / 2f, strokePx / 2f),
            size = Size(w - strokePx, h - strokePx),
            cornerRadius = CornerRadius(cornerPx, cornerPx),
            style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
    }
}

/**
 * 4c. Ultra-Smooth 8-Mode Dynamic Island Motion & Shader Physics Modifier
 * Combines 60/120Hz frame-synchronized motion physics + surface shaders so the Dynamic Island
 * feels alive, fluid, and responsive.
 */
fun Modifier.dynamicIslandMotionEffect(
    enabled: Boolean = true,
    style: DynamicIslandMotionStyle = DynamicIslandMotionStyle.LIQUID_SPRING,
    isConnected: Boolean = false,
    isExpanded: Boolean = false,
    trafficRateBytesPerSec: Long = 0L,
    accentColor: Color = Color(0xFFFF6D00),
    cornerRadius: Dp = 28.dp,
): Modifier = composed {
    if (!enabled) return@composed this

    var phaseRad by remember { mutableFloatStateOf(0f) }
    var shimmerProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(style, isConnected) {
        var lastMs = withInfiniteAnimationFrameMillis { it }
        while (isActive) {
            val nowMs = withInfiniteAnimationFrameMillis { it }
            val dt = ((nowMs - lastMs).coerceIn(1L, 64L)) / 1000f
            lastMs = nowMs
            val speed = if (isConnected) 2.4f else 1.5f
            phaseRad = (phaseRad + dt * speed) % (20f * PI.toFloat())
            shimmerProgress = (shimmerProgress + dt * 0.55f) % 1f
        }
    }

    val trafficFactor = (trafficRateBytesPerSec.toFloat() / (512f * 1024f)).coerceIn(0f, 1f)

    this
        .graphicsLayer {
            when (style) {
                DynamicIslandMotionStyle.LIQUID_SPRING -> {
                    val s = if (isExpanded) 1f else 1f + 0.012f * sin(phaseRad * 1.1f)
                    scaleX = s
                    scaleY = s
                    translationY = sin(phaseRad * 0.9f) * 2.2f
                }
                DynamicIslandMotionStyle.ZERO_GRAVITY -> {
                    translationY = sin(phaseRad * 0.85f) * 5.5f
                    translationX = cos(phaseRad * 0.6f) * 2.8f
                    rotationZ = if (isExpanded) 0f else sin(phaseRad * 0.5f) * 0.75f
                }
                DynamicIslandMotionStyle.BREATHING_HALO -> {
                    val breath = 1f + 0.018f * ((sin(phaseRad) + 1f) * 0.5f)
                    scaleX = if (isExpanded) 1f else breath
                    scaleY = if (isExpanded) 1f else breath
                }
                DynamicIslandMotionStyle.SONIC_WAVE_SYNC -> {
                    val dynamicAmp = 0.008f + trafficFactor * 0.032f
                    val s = if (isExpanded) 1f else 1f + sin(phaseRad * 2.6f) * dynamicAmp
                    scaleX = s
                    scaleY = s
                }
                DynamicIslandMotionStyle.HEARTBEAT_PULSE -> {
                    // Smooth cardiac lub-dub envelope
                    val t = (phaseRad * 0.45f) % 1f
                    val beat = when {
                        t < 0.12f -> sin((t / 0.12f) * PI.toFloat()) * 0.026f
                        t in 0.18f..0.30f -> sin(((t - 0.18f) / 0.12f) * PI.toFloat()) * 0.016f
                        else -> 0f
                    }
                    val s = if (isExpanded) 1f else 1f + beat
                    scaleX = s
                    scaleY = s
                }
                DynamicIslandMotionStyle.PRISM_SHIMMER -> {
                    translationY = sin(phaseRad * 0.75f) * 1.8f
                }
                DynamicIslandMotionStyle.MAGNETIC_ELASTIC -> {
                    val stretchX = if (isExpanded) 1f else 1f + sin(phaseRad * 1.2f) * 0.016f
                    val stretchY = if (isExpanded) 1f else 1f - sin(phaseRad * 1.2f) * 0.012f
                    scaleX = stretchX
                    scaleY = stretchY
                }
                DynamicIslandMotionStyle.STARLIGHT_ORBIT -> {
                    translationY = sin(phaseRad * 0.8f) * 2.5f
                }
            }
        }
        .drawWithContent {
            drawContent()
            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@drawWithContent
            val rPx = cornerRadius.toPx()

            when (style) {
                DynamicIslandMotionStyle.PRISM_SHIMMER,
                DynamicIslandMotionStyle.LIQUID_SPRING -> {
                    // Soft diagonal specular silk sheen sweeping across the island
                    val sweepX = (-0.35f + shimmerProgress * 1.7f) * w
                    val shimmerAlpha = if (style == DynamicIslandMotionStyle.PRISM_SHIMMER) 0.22f else 0.10f
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = shimmerAlpha),
                                accentColor.copy(alpha = shimmerAlpha * 0.65f),
                                Color.Transparent,
                            ),
                            start = Offset(sweepX - w * 0.25f, 0f),
                            end = Offset(sweepX + w * 0.25f, h),
                        ),
                        cornerRadius = CornerRadius(rPx, rPx),
                    )
                }
                DynamicIslandMotionStyle.BREATHING_HALO,
                DynamicIslandMotionStyle.SONIC_WAVE_SYNC -> {
                    val haloAlpha = (0.14f + 0.14f * sin(phaseRad * 1.5f) + trafficFactor * 0.15f).coerceIn(0.05f, 0.38f)
                    drawRoundRect(
                        color = accentColor.copy(alpha = haloAlpha),
                        topLeft = Offset(1.5.dp.toPx(), 1.5.dp.toPx()),
                        size = Size(w - 3.dp.toPx(), h - 3.dp.toPx()),
                        cornerRadius = CornerRadius(rPx, rPx),
                        style = Stroke(width = 1.6.dp.toPx()),
                    )
                }
                DynamicIslandMotionStyle.STARLIGHT_ORBIT -> {
                    // 4 orbiting starlight micro-particles gliding along the Dynamic Island
                    val cx = w / 2f
                    val cy = h / 2f
                    val rx = (w / 2f) - 4.dp.toPx()
                    val ry = (h / 2f) - 4.dp.toPx()
                    for (idx in 0 until 4) {
                        val a = phaseRad * 0.9f + idx * (PI.toFloat() / 2f)
                        val px = cx + rx * cos(a)
                        val py = cy + ry * sin(a)
                        drawCircle(
                            color = accentColor.copy(alpha = 0.45f),
                            radius = 4.5.dp.toPx(),
                            center = Offset(px, py),
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.92f),
                            radius = 1.8.dp.toPx(),
                            center = Offset(px, py),
                        )
                    }
                }
                else -> Unit
            }
        }
}

/**
 * 5. 2026-Trend Harmonic Fluid Wave Aura & Radar Pulse
 * Enlarged, multi-layered organic sine-modulated wave rings with ambient glow and orbital nodes.
 */
@Composable
fun NeumorphicRadarPulse(
    color: Color,
    isActive: Boolean,
    isConnecting: Boolean = false,
    size: Dp = 236.dp,
    modifier: Modifier = Modifier,
    centerContent: @Composable () -> Unit = {},
) {
    val infinite = rememberInfiniteTransition(label = "harmonic-wave-2026")
    val wavePhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(if (isConnecting) 1500 else 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "wave-phase",
    )
    val rippleProgress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isConnecting) 1300 else 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ripple-progress",
    )
    val breathScale by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breath-scale",
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val maxRadius = this.size.minDimension / 2f

            // Ambient soft radial aura glow (2026 glass/neon depth)
            val auraAlpha = when {
                isActive -> 0.25f
                isConnecting -> 0.22f
                else -> 0.10f
            }
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = auraAlpha),
                        color.copy(alpha = auraAlpha * 0.42f),
                        Color.Transparent,
                    ),
                    center = center,
                    radius = maxRadius * breathScale,
                ),
                radius = maxRadius * breathScale,
                center = center,
            )

            // 4 Large Organic 2026 Harmonic Wave Layers
            val layerCount = 4
            for (layer in 0 until layerCount) {
                val layerOffset = layer * 0.25f
                val progress = (rippleProgress + layerOffset) % 1f
                val baseR = maxRadius * (0.38f + 0.60f * progress)
                val alphaEnvelope = sin(progress * PI.toFloat()).coerceIn(0f, 1f)
                val ringAlpha = alphaEnvelope * (if (isActive || isConnecting) 0.56f else 0.24f)

                val wavePath = Path()
                val steps = 84
                val lobeCount = if (layer % 2 == 0) 6 else 8
                val amplitude = maxRadius * (if (isActive || isConnecting) 0.036f else 0.016f) * (1f + layer * 0.22f)
                val dir = if (layer % 2 == 0) 1f else -1f

                for (i in 0..steps) {
                    val theta = (i.toFloat() / steps) * (2f * PI.toFloat())
                    val harmonic = sin(lobeCount * theta + dir * wavePhase + layer * 0.9f) * amplitude +
                        cos((lobeCount / 2) * theta - dir * wavePhase * 0.75f) * (amplitude * 0.48f)
                    val r = baseR + harmonic
                    val x = center.x + r * cos(theta)
                    val y = center.y + r * sin(theta)
                    if (i == 0) {
                        wavePath.moveTo(x, y)
                    } else {
                        wavePath.lineTo(x, y)
                    }
                }
                wavePath.close()

                drawPath(
                    path = wavePath,
                    color = color.copy(alpha = ringAlpha),
                    style = Stroke(
                        width = (2.6f - layer * 0.42f).coerceAtLeast(1.1f).dp.toPx(),
                        cap = StrokeCap.Round,
                    ),
                )
            }

            // Inner subtle precision ring + rotating orbital satellites when active
            val innerRingR = maxRadius * 0.42f * breathScale
            drawCircle(
                color = color.copy(alpha = if (isActive || isConnecting) 0.32f else 0.14f),
                radius = innerRingR,
                center = center,
                style = Stroke(width = 1.4.dp.toPx()),
            )

            if (isActive || isConnecting) {
                for (orb in 0 until 3) {
                    val orbAngle = wavePhase + (orb * 2f * PI.toFloat() / 3f)
                    val orbRadius = maxRadius * (0.58f + orb * 0.12f)
                    val ox = center.x + orbRadius * cos(orbAngle)
                    val oy = center.y + orbRadius * sin(orbAngle)
                    drawCircle(
                        color = color.copy(alpha = 0.65f),
                        radius = 3.2.dp.toPx(),
                        center = Offset(ox, oy),
                    )
                }
            }

            if (isConnecting) {
                val deg = (wavePhase / (2f * PI.toFloat())) * 360f
                rotate(deg, pivot = center) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            0f to Color.Transparent,
                            0.60f to color.copy(alpha = 0.06f),
                            1f to color.copy(alpha = 0.52f),
                            center = center,
                        ),
                        startAngle = 0f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(center.x - maxRadius * 0.76f, center.y - maxRadius * 0.76f),
                        size = Size(maxRadius * 1.52f, maxRadius * 1.52f),
                        style = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
            }
        }
        centerContent()
    }
}

/**
 * 6. 3D Interactive World Globe & Cyber Projection Background
 * Renders a 3D spherical globe + continental dot projection whose 3D camera pitch/tilt
 * and longitude rotation respond in real-time as the user scrolls the screen.
 */
private val WORLD_MAP_BITMAP: List<String> = listOf(
    "................................................",
    "......#####....####..............#####..........",
    "....########..#####...........#############.....",
    "...##########...##..........################....",
    "...###########.............##################...",
    "....#########......###....###################...",
    ".....######.......#####...###################...",
    "......####........#####....#################....",
    ".......###.........#######..###############.....",
    "........###........#######...#############......",
    ".........#####.....#######....###########.......",
    "..........######....######.....########..##.....",
    "..........#######...######......#####....###....",
    "...........######....#####.......###............",
    "...........#####.....####.................####..",
    "............####......###................######.",
    "............###.......##.................######.",
    ".............##...........................####..",
    "................................................",
)

// (Lon in radians -PI..PI, Lat in radians -PI/2..PI/2) for major global VPN hubs
private val GLOBE_HUB_COORDS: List<Pair<Float, Float>> = listOf(
    Pair(-1.29f, 0.71f),  // New York
    Pair(-2.13f, 0.65f),  // Los Angeles
    Pair(0.15f, 0.88f),   // Frankfurt / Brussels
    Pair(-0.02f, 0.90f),  // London / Amsterdam
    Pair(0.43f, 1.05f),   // Helsinki / Stockholm
    Pair(1.81f, 0.02f),   // Singapore
    Pair(2.44f, 0.62f),   // Tokyo
    Pair(2.64f, -0.59f),  // Sydney
)

@Composable
fun AnimatedWorldMapBackground(
    isConnected: Boolean,
    isConnecting: Boolean,
    accentColor: Color,
    isDark: Boolean,
    scrollOffsetPx: Float = 0f,
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "world-globe-3d")
    val autoRotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isConnecting) 16000 else 32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "globe-auto-rot",
    )
    val scanWave by infinite.animateFloat(
        initialValue = -0.25f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isConnecting) 3000 else 5800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "globe-scan-wave",
    )
    val nodePulse by infinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "globe-node-pulse",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        // Scroll-driven 3D camera angles:
        // Scrolling changes both the 3D pitch (tiltAngle) and yaw (longitude rotation)
        val scrollNormalized = (scrollOffsetPx / 900f)
        val tiltAngle = (0.32f + scrollNormalized * 0.65f).coerceIn(-0.65f, 0.85f)
        val cosTilt = cos(tiltAngle)
        val sinTilt = sin(tiltAngle)
        val yawAngle = autoRotation + scrollNormalized * 1.35f

        val globeRadius = (minOf(w, h) * 0.44f).coerceIn(145.dp.toPx(), 260.dp.toPx())
        val globeCenter = Offset(
            x = w * 0.5f,
            y = (210.dp.toPx() - scrollOffsetPx * 0.08f).coerceIn(130.dp.toPx(), h * 0.45f),
        )

        // Helper to project 3D spherical coordinates (lon, lat) onto 2D screen with depth z (-1..1)
        fun project3D(lon: Float, lat: Float, radiusScale: Float = 1f): Triple<Float, Float, Float> {
            val relLon = lon + yawAngle
            val x3 = cos(lat) * sin(relLon)
            val y3 = sin(lat)
            val z3 = cos(lat) * cos(relLon)

            // Apply 3D camera pitch (rotation around X-axis)
            val yRot = y3 * cosTilt - z3 * sinTilt
            val zRot = y3 * sinTilt + z3 * cosTilt

            val r = globeRadius * radiusScale
            val sx = globeCenter.x + x3 * r
            val sy = globeCenter.y - yRot * r
            return Triple(sx, sy, zRot)
        }

        // 1. Ambient 3D Planetary Aura Glow
        val auraAlpha = if (isConnected) 0.16f else if (isConnecting) 0.14f else 0.07f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    accentColor.copy(alpha = auraAlpha),
                    accentColor.copy(alpha = auraAlpha * 0.35f),
                    Color.Transparent,
                ),
                center = globeCenter,
                radius = globeRadius * 1.28f,
            ),
            radius = globeRadius * 1.28f,
            center = globeCenter,
        )

        // Outer 3D Sphere Rim
        drawCircle(
            color = accentColor.copy(alpha = if (isDark) 0.16f else 0.14f),
            radius = globeRadius,
            center = globeCenter,
            style = Stroke(width = 1.2.dp.toPx()),
        )

        // 2. 3D Latitude & Longitude Wireframe Meridians (rotates and tilts with scroll)
        val wireAlpha = if (isDark) 0.075f else 0.085f
        for (latIdx in -2..2) {
            val lat = latIdx * (PI.toFloat() / 6f)
            val path = Path()
            var started = false
            val steps = 48
            for (s in 0..steps) {
                val lon = -PI.toFloat() + (s.toFloat() / steps) * (2f * PI.toFloat())
                val (sx, sy, z) = project3D(lon, lat)
                if (z >= -0.15f) {
                    if (!started) {
                        path.moveTo(sx, sy)
                        started = true
                    } else {
                        path.lineTo(sx, sy)
                    }
                } else {
                    started = false
                }
            }
            drawPath(
                path = path,
                color = accentColor.copy(alpha = wireAlpha),
                style = Stroke(width = 0.85.dp.toPx()),
            )
        }

        for (lonIdx in 0 until 8) {
            val lon = (lonIdx.toFloat() / 8f) * (2f * PI.toFloat())
            val path = Path()
            var started = false
            val steps = 36
            for (s in 0..steps) {
                val lat = -(PI.toFloat() / 2f) + (s.toFloat() / steps) * PI.toFloat()
                val (sx, sy, z) = project3D(lon, lat)
                if (z >= -0.15f) {
                    if (!started) {
                        path.moveTo(sx, sy)
                        started = true
                    } else {
                        path.lineTo(sx, sy)
                    }
                } else {
                    started = false
                }
            }
            drawPath(
                path = path,
                color = accentColor.copy(alpha = wireAlpha),
                style = Stroke(width = 0.85.dp.toPx()),
            )
        }

        // 3. 3D Continental Dot Matrix Mapped onto Rotating Sphere
        val rows = WORLD_MAP_BITMAP.size
        val cols = WORLD_MAP_BITMAP.first().length
        val baseDotRadius = 2.4.dp.toPx()

        for (r in 0 until rows) {
            val rowStr = WORLD_MAP_BITMAP[r]
            val normY = r.toFloat() / (rows - 1).toFloat()
            val lat = ((0.5f - normY) * PI.toFloat() * 0.82f)
            for (c in 0 until cols) {
                val isLand = rowStr[c] == '#'
                if (!isLand && (r + c) % 6 != 0) continue

                val normX = c.toFloat() / cols.toFloat()
                val lon = (normX - 0.5f) * (2f * PI.toFloat())
                val (sx, sy, z) = project3D(lon, lat)

                if (z > -0.25f) {
                    val depthFactor = ((z + 0.25f) / 1.25f).coerceIn(0.1f, 1f)
                    val distToWave = abs(((normX + normY * 0.2f) % 1f) - scanWave)
                    val waveGlow = (1f - (distToWave / 0.20f)).coerceIn(0f, 1f)

                    if (isLand) {
                        val baseAlpha = if (isDark) 0.22f else 0.20f
                        val activeBoost = if (isConnected || isConnecting) 0.34f else 0.16f
                        val dotAlpha = ((baseAlpha + waveGlow * activeBoost) * depthFactor).coerceIn(0.04f, 0.78f)
                        drawCircle(
                            color = accentColor.copy(alpha = dotAlpha),
                            radius = baseDotRadius * (0.65f + 0.55f * depthFactor + waveGlow * 0.25f),
                            center = Offset(sx, sy),
                        )
                    } else if (z > 0.1f) {
                        drawCircle(
                            color = accentColor.copy(alpha = (if (isDark) 0.045f else 0.05f) * depthFactor),
                            radius = baseDotRadius * 0.42f,
                            center = Offset(sx, sy),
                        )
                    }
                }
            }
        }

        // 4. 3D Orbital Satellite Ring & Global VPN Hub Arcs
        val orbitPath = Path()
        val orbitSteps = 60
        for (i in 0..orbitSteps) {
            val theta = (i.toFloat() / orbitSteps) * (2f * PI.toFloat())
            val (ox, oy, _) = project3D(theta, sin(theta) * 0.28f, radiusScale = 1.14f)
            if (i == 0) orbitPath.moveTo(ox, oy) else orbitPath.lineTo(ox, oy)
        }
        drawPath(
            path = orbitPath,
            color = accentColor.copy(alpha = if (isConnected || isConnecting) 0.20f else 0.09f),
            style = Stroke(
                width = 1.1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), phase = scanWave * 220f),
            ),
        )

        val projectedHubs = GLOBE_HUB_COORDS.map { (lon, lat) ->
            project3D(lon, lat, radiusScale = 1.01f)
        }

        val primaryHub = projectedHubs[2] // Europe hub
        val arcAlpha = if (isConnected) 0.36f else if (isConnecting) 0.28f else 0.13f
        projectedHubs.forEachIndexed { idx, target ->
            if (idx != 2 && (primaryHub.third > -0.2f || target.third > -0.2f)) {
                val midX = (primaryHub.first + target.first) / 2f
                val dist = sqrt(
                    (target.first - primaryHub.first) * (target.first - primaryHub.first) +
                        (target.second - primaryHub.second) * (target.second - primaryHub.second)
                )
                val midY = minOf(primaryHub.second, target.second) - dist * 0.26f
                val arcPath = Path().apply {
                    moveTo(primaryHub.first, primaryHub.second)
                    quadraticTo(midX, midY, target.first, target.second)
                }
                drawPath(
                    path = arcPath,
                    color = accentColor.copy(alpha = arcAlpha),
                    style = Stroke(
                        width = 1.4.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 8f),
                            phase = (scanWave * 180f + idx * 28f),
                        ),
                    ),
                )
            }
        }

        projectedHubs.forEachIndexed { idx, (hx, hy, hz) ->
            if (hz > -0.15f) {
                val isPrimary = idx == 2 || idx == 3
                val visibility = ((hz + 0.2f) / 1.2f).coerceIn(0.3f, 1f)
                val pulseR = (if (isPrimary) 8.5.dp.toPx() else 5.5.dp.toPx()) * nodePulse * visibility
                drawCircle(
                    color = accentColor.copy(alpha = (if (isPrimary) 0.34f else 0.20f) * nodePulse * visibility),
                    radius = pulseR,
                    center = Offset(hx, hy),
                )
                drawCircle(
                    color = accentColor.copy(alpha = (if (isPrimary) 0.85f else 0.55f) * visibility),
                    radius = (if (isPrimary) 3.4.dp.toPx() else 2.4.dp.toPx()) * visibility,
                    center = Offset(hx, hy),
                )
            }
        }
    }
}

/**
 * 7. Odometer-style Animated Text for Timers & Counters
 */
@Composable
fun AnimatedOdometerText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        text.forEachIndexed { index, char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    (slideInVertically { height -> height / 2 } + fadeIn(tween(180)))
                        .togetherWith(slideOutVertically { height -> -height / 2 } + fadeOut(tween(180)))
                        .using(SizeTransform(clip = false))
                },
                label = "odometer-char-$index",
            ) { targetChar ->
                Text(
                    text = targetChar.toString(),
                    style = style,
                    color = color,
                )
            }
        }
    }
}

/**
 * 8. Dual-Line Live Traffic Waveform Sparkline Canvas
 */
@Composable
fun LiveTrafficSparkline(
    rxHistory: List<Long>,
    txHistory: List<Long>,
    rxColor: Color,
    txColor: Color,
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "wave-phase")
    val waveShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "wave-shift",
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@Canvas

        val maxVal = maxOf(
            rxHistory.maxOrNull() ?: 1024L,
            txHistory.maxOrNull() ?: 1024L,
            4096L,
        ).toFloat()

        fun buildSmoothPath(points: List<Long>, phaseOffset: Float): Path {
            val path = Path()
            val count = points.size.coerceAtLeast(2)
            val stepX = w / (count - 1).toFloat()
            for (i in 0 until count) {
                val sample = points.getOrElse(i) { 0L }.toFloat()
                val normalized = (sample / maxVal).coerceIn(0f, 1f)
                val idleBreath = sin(waveShift + phaseOffset + i * 0.45f) * 0.04f
                val yFactor = (normalized * 0.78f + 0.10f + idleBreath).coerceIn(0.06f, 0.92f)
                val x = i * stepX
                val y = h - (yFactor * h)
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    val prevX = (i - 1) * stepX
                    val prevSample = points.getOrElse(i - 1) { 0L }.toFloat()
                    val prevNorm = (prevSample / maxVal).coerceIn(0f, 1f)
                    val prevBreath = sin(waveShift + phaseOffset + (i - 1) * 0.45f) * 0.04f
                    val prevY = h - ((prevNorm * 0.78f + 0.10f + prevBreath).coerceIn(0.06f, 0.92f) * h)
                    val ctrlX = (prevX + x) / 2f
                    path.cubicTo(ctrlX, prevY, ctrlX, y, x, y)
                }
            }
            return path
        }

        val rxPath = buildSmoothPath(rxHistory, 0f)
        val txPath = buildSmoothPath(txHistory, 1.6f)

        // Fill under RX curve
        val fillPath = Path().apply {
            addPath(rxPath)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(rxColor.copy(alpha = 0.28f), Color.Transparent),
                startY = 0f,
                endY = h,
            ),
        )

        drawPath(
            path = txPath,
            color = txColor.copy(alpha = 0.75f),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
        )
        drawPath(
            path = rxPath,
            color = rxColor,
            style = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round),
        )
    }
}

/**
 * 9a. Animated Broadcasting Signal Tower Wave (For Dynamic Island)
 * Renders a central radio/cell signal tower mast with glowing beacon and outward-propagating
 * electromagnetic wave arcs on both sides ((( T ))).
 */
@Composable
fun AnimatedSignalTowerWave(
    active: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "signal-tower-wave")
    val waveExpand by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (active) 1350 else 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "tower-wave-expand",
    )
    val beaconPulse by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "tower-beacon-pulse",
    )

    Canvas(
        modifier = modifier
            .width(26.dp)
            .height(18.dp),
    ) {
        val cx = size.width / 2f
        val cy = size.height * 0.42f
        val maxWaveRadius = size.width * 0.46f

        // Draw Tower Base & Mast (A-frame radio tower)
        val towerBottom = size.height * 0.94f
        val towerBaseHalfW = size.width * 0.14f
        val towerPath = Path().apply {
            moveTo(cx, cy)
            lineTo(cx - towerBaseHalfW, towerBottom)
            moveTo(cx, cy)
            lineTo(cx + towerBaseHalfW, towerBottom)
            // Crossbar
            val crossY = cy + (towerBottom - cy) * 0.58f
            val crossHalfW = towerBaseHalfW * 0.6f
            moveTo(cx - crossHalfW, crossY)
            lineTo(cx + crossHalfW, crossY)
        }
        drawPath(
            path = towerPath,
            color = color.copy(alpha = if (active) 0.95f else 0.5f),
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round),
        )

        // Glowing antenna emitter tip
        drawCircle(
            color = color.copy(alpha = if (active) beaconPulse else 0.45f),
            radius = 2.3.dp.toPx(),
            center = Offset(cx, cy),
        )

        // 3 pairs of left & right radiating electromagnetic signal arcs ((( • )))
        val waveCount = 3
        for (i in 0 until waveCount) {
            val phase = (waveExpand + i / waveCount.toFloat()) % 1f
            val r = 3.dp.toPx() + phase * (maxWaveRadius - 3.dp.toPx())
            val alpha = (1f - phase) * (if (active) 0.92f else 0.32f)
            val arcTopLeft = Offset(cx - r, cy - r)
            val arcSize = Size(r * 2f, r * 2f)
            val stroke = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)

            // Right broadcast wave arc (-45 deg to +45 deg)
            drawArc(
                color = color.copy(alpha = alpha),
                startAngle = -42f,
                sweepAngle = 84f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = stroke,
            )
            // Left broadcast wave arc (138 deg to 222 deg)
            drawArc(
                color = color.copy(alpha = alpha),
                startAngle = 138f,
                sweepAngle = 84f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = stroke,
            )
        }
    }
}

/**
 * 9b. Animated Equalizer Activity Bars (Secondary helper)
 */
@Composable
fun AnimatedTrafficEqualizer(
    active: Boolean,
    color: Color,
    barCount: Int = 4,
    modifier: Modifier = Modifier,
) {
    AnimatedSignalTowerWave(
        active = active,
        color = color,
        modifier = modifier,
    )
}

/**
 * 10. Neumorphic Speedometer Analog + Digital Gauge Canvas
 * Needle is drawn in the outer ring band (0.34R..0.74R) so it never overlaps the center/bottom text.
 */
@Composable
fun NeumorphicSpeedometerGauge(
    speedMbps: Float,
    maxMbps: Float = 100f,
    accentColor: Color,
    trackColor: Color,
    isRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    val animatedSpeed by animateFloatAsState(
        targetValue = speedMbps.coerceIn(0f, maxMbps),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "speed-gauge-needle",
    )

    val infinite = rememberInfiniteTransition(label = "gauge-glow")
    val glowPulse by infinite.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "gauge-glow-val",
    )

    Canvas(modifier = modifier) {
        val strokeWidth = 14.dp.toPx()
        val diameter = size.minDimension - strokeWidth * 2f
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = diameter / 2f

        // Background track arc (135 deg to 405 deg -> 270 deg sweep)
        drawArc(
            color = trackColor,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )

        // Tick marks
        val tickCount = 28
        for (i in 0..tickCount) {
            val fraction = i.toFloat() / tickCount
            val angleDeg = 135f + fraction * 270f
            val angleRad = angleDeg * (PI / 180.0)
            val isMajor = i % 4 == 0
            val innerR = radius - strokeWidth * (if (isMajor) 1.4f else 1.1f)
            val outerR = radius - strokeWidth * 0.7f
            val start = Offset(
                x = center.x + (innerR * cos(angleRad)).toFloat(),
                y = center.y + (innerR * sin(angleRad)).toFloat(),
            )
            val end = Offset(
                x = center.x + (outerR * cos(angleRad)).toFloat(),
                y = center.y + (outerR * sin(angleRad)).toFloat(),
            )
            drawLine(
                color = trackColor.copy(alpha = if (isMajor) 0.9f else 0.45f),
                start = start,
                end = end,
                strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.2.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        val sweepFraction = (animatedSpeed / maxMbps).coerceIn(0f, 1f)
        val activeSweep = sweepFraction * 270f

        if (activeSweep > 0.5f) {
            drawArc(
                color = accentColor.copy(alpha = if (isRunning) glowPulse else 0.9f),
                startAngle = 135f,
                sweepAngle = activeSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
        }

        // Outer-band floating needle (starts outside center readout zone so text never overlaps)
        val needleAngleRad = (135f + activeSweep) * (PI / 180.0)
        val needleStartLen = radius * 0.42f
        val needleEndLen = radius * 0.76f
        val needleBase = Offset(
            x = center.x + (needleStartLen * cos(needleAngleRad)).toFloat(),
            y = center.y + (needleStartLen * sin(needleAngleRad)).toFloat(),
        )
        val needleTip = Offset(
            x = center.x + (needleEndLen * cos(needleAngleRad)).toFloat(),
            y = center.y + (needleEndLen * sin(needleAngleRad)).toFloat(),
        )
        drawLine(
            color = accentColor,
            start = needleBase,
            end = needleTip,
            strokeWidth = 3.6.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawCircle(
            color = accentColor,
            radius = 4.2.dp.toPx(),
            center = needleBase,
        )
    }
}
