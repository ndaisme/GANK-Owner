package com.example.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceStatus
import com.example.ui.theme.GankColors

@Composable
fun NeoBrutalistCard(
    modifier: Modifier = Modifier,
    shadowOffset: Dp = 5.dp,
    borderWidth: Dp = 3.dp,
    cornerRadius: Dp = 8.dp,
    backgroundColor: Color = GankColors.White,
    borderColor: Color = GankColors.Ink,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Solid Pitch Black Hard Offset Shadow (matches content height)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .clip(RoundedCornerShape(cornerRadius))
                .background(GankColors.Ink)
        )
        // Main Foreground Content Container (defines size)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
                .padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun NeoBrutalistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = GankColors.GankYellow,
    contentColor: Color = GankColors.Ink,
    icon: ImageVector? = null,
    testTag: String = "neo_button"
) {
    var isPressed by remember { mutableStateOf(false) }
    val shadowOffset by animateDpAsState(if (isPressed) 0.dp else 4.dp, label = "shadowOffset")
    val contentOffset by animateDpAsState(if (isPressed) 4.dp else 0.dp, label = "contentOffset")

    Box(modifier = modifier.testTag(testTag)) {
        // Shadow Box
        Box(
            Modifier
                .matchParentSize()
                .background(GankColors.Ink, RoundedCornerShape(8.dp))
        )
        // Button Content Box
        Box(
            Modifier
                .offset(x = contentOffset, y = contentOffset)
                .background(containerColor, RoundedCornerShape(8.dp))
                .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp).padding(end = 6.dp)
                    )
                }
                Text(
                    text = text,
                    fontWeight = FontWeight.Black,
                    color = contentColor,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun NeoBrutalistTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    testTag: String = "neo_input"
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = GankColors.Ink,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = GankColors.Steel) },
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GankColors.White,
                unfocusedContainerColor = GankColors.White,
                focusedBorderColor = GankColors.Ink,
                unfocusedBorderColor = GankColors.Ink,
                focusedTextColor = GankColors.Ink,
                unfocusedTextColor = GankColors.Ink
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .testTag(testTag)
        )
    }
}

@Composable
fun GSStatusChip(
    status: ServiceStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        ServiceStatus.CHECK_IN -> GankColors.Silver to GankColors.Ink
        ServiceStatus.DIAGNOSIS -> GankColors.NeonBlue to GankColors.Ink
        ServiceStatus.WAITING_APPROVAL -> GankColors.Warning to GankColors.White
        ServiceStatus.REPAIR -> GankColors.GankYellow to GankColors.Ink
        ServiceStatus.QUALITY_CONTROL -> GankColors.PurpleQC to GankColors.White
        ServiceStatus.COMPLETED -> GankColors.Success to GankColors.White
        ServiceStatus.PICKED_UP -> GankColors.Ink to GankColors.White
        ServiceStatus.CANCELLED -> GankColors.Danger to GankColors.White
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = textColor
        )
    }
}

@Composable
fun NeoSectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(8.dp)
                .height(24.dp)
                .background(GankColors.GankYellow)
                .border(2.dp, GankColors.Ink)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = GankColors.Ink
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = GankColors.Steel
                )
            }
        }
    }
}
