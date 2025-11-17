package com.rajotiyapawan.androidpractice

/**
 * Created by Pawan Rajotiya on 11-11-2025.
 */

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrueInnerShadowBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = Color.White,
    shadowColor: Color = Color.Black.copy(alpha = 0.35f),
    blur: Dp = 10.dp,
    offsetX: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    content: @Composable () -> Unit = {}
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val radiusPx = cornerRadius.toPx()
            val blurPx = blur.toPx()
            val offsetXPx = offsetX.toPx()
            val offsetYPx = offsetY.toPx()

            val rect = Rect(Offset.Zero, size)
            val roundRect = RoundRect(rect, CornerRadius(radiusPx))

            // Create a blurred mask shadow
            val shadowPaint = Paint().apply {
                color = shadowColor
                asFrameworkPaint().apply {
                    isAntiAlias = true
                    maskFilter = android.graphics.BlurMaskFilter(
                        blurPx,
                        android.graphics.BlurMaskFilter.Blur.NORMAL
                    )
                }
            }

            // Draw a blurred shape offset in the opposite direction
            drawIntoCanvas { canvas ->
                val layerRect = Rect(Offset.Zero, size)
                canvas.saveLayer(layerRect, Paint())

                // Offset the shape (simulate light direction)
                val shadowPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(offsetXPx, offsetYPx),
                                size = size
                            ),
                            cornerRadius = CornerRadius(radiusPx)
                        )
                    )
                }

                canvas.drawPath(shadowPath, shadowPaint)

                // Punch out the actual box area so only the inner shadow remains
                val clearPaint = Paint().apply { blendMode = BlendMode.Clear }
                val cutoutPath = Path().apply { addRoundRect(roundRect) }
                canvas.drawPath(cutoutPath, clearPaint)

                canvas.restore()
            }
        }

        content()
    }
}


@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
fun InnerShadowPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(2.dp)
            .background(Color(0xfff5f5f5), RoundedCornerShape(20.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .padding(top = 36.dp)
                .size(108.dp)
                .background(Color.Gray)
        )
        Text("Introducing", modifier = Modifier.padding(top = 3.dp), fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Text(buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xff9902a7), Color(0xffd0252e), Color(0xffc0165a))
                    )
                )
            ) {
                append("AI Property Assistant")
            }
        }, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text(
            "A 10x faster & smarter way to search",
            modifier = Modifier.padding(top = 3.dp),
            fontSize = 14.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.SemiBold
        )
        Box(
            Modifier
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Choose your AI search mode",
                    modifier = Modifier,
                    fontSize = 14.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.padding(top = 8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Color(0xffe8e8e8), shape = RoundedCornerShape(50))
                        .background(Color.White)
                        .padding(vertical = 7.dp), contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(Icons.Default.MailOutline, contentDescription = null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Chat", color = Color(0xffd8232a), modifier = Modifier,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(brush = Brush.linearGradient(colors = listOf(Color.White, Color(0xffd7d7d7))))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("or")
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(brush = Brush.linearGradient(colors = listOf(Color(0xffd7d7d7), Color.White)))
                    )
                }
                Spacer(Modifier.height(12.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 34.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                            .innerShadow(
                                shape = RoundedCornerShape(20.dp),
                                shadow = androidx.compose.ui.graphics.shadow.Shadow(
                                    radius = 5.dp, color = Color.Black.copy(alpha = 0.2f)
                                )
                            )
                            .padding(start = 24.dp, end = 24.dp, bottom = 30.dp, top = 44.dp)

                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xffd8232a), shape = RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Voice", color = Color.White, modifier = Modifier.padding(8.dp))
                        }
                    }

                    Box(
                        Modifier
                            .size(68.dp)
                            .border(1.dp, color = Color(0x33909090), shape = CircleShape)
                            .background(color = Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            tint = Color(0xff009681),
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }
        }
    }
}
