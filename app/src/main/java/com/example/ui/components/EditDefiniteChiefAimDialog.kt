package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private val CHIEF_AIM_TEMPLATES = listOf(
    "By December 31, I will accumulate $100,000 in wealth through delivering exceptional value and mastering my craft.",
    "My Definite Chief Aim is to build a sovereign, high-impact enterprise that grants financial freedom and elevates my family's legacy.",
    "I demand of myself continuous, persistent action toward my goal of becoming a master architect of wealth and unwavering discipline.",
    "I have in my mind a clear picture of my financial independence, and I will daily transmute this desire into its physical equivalent."
)

@Composable
fun EditDefiniteChiefAimDialog(
    initialStatement: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var statementText by remember { mutableStateOf(initialStatement) }
    var selectedTemplateIndex by remember { mutableStateOf<Int?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .border(1.5.dp, Brush.linearGradient(listOf(GoldLight, GoldDark, AmberBright)), RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .testTag("edit_chief_aim_dialog"),
        containerColor = RichBlack,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(AmberBright, GoldDark))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = null,
                            tint = RichBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "DEFINITE CHIEF AIM",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Your Transmutation Goal Statement",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("close_chief_aim_dialog")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Napoleon Hill's First Law of Success: Formulate a clear, concise written statement of the exact amount of money or major purpose you intend to acquire.",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextSecondary,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = statementText,
                    onValueChange = { statementText = it },
                    placeholder = {
                        Text(
                            text = "e.g., 'By [Date], I will acquire [Specific Goal/Amount]. In return, I will give the most efficient service of which I am capable...'",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    },
                    minLines = 4,
                    maxLines = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chief_aim_text_field"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = DarkBorder,
                        focusedContainerColor = DarkCharcoal.copy(alpha = 0.6f),
                        unfocusedContainerColor = DarkCharcoal.copy(alpha = 0.4f),
                        cursorColor = AmberBright
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${statementText.length} characters",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    if (statementText.isNotBlank()) {
                        Text(
                            text = "Ready to inscribe ✨",
                            fontSize = 10.sp,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = AmberBright,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Or choose a classical formula:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                CHIEF_AIM_TEMPLATES.forEachIndexed { index, template ->
                    val isSelected = selectedTemplateIndex == index
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) GoldDark.copy(alpha = 0.35f) else SurfaceElevated.copy(alpha = 0.6f))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) GoldPrimary else DarkBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                selectedTemplateIndex = index
                                statementText = template
                            }
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Filled.FormatQuote,
                                contentDescription = null,
                                tint = if (isSelected) AmberBright else TextMuted,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = template,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (statementText.isNotBlank()) {
                        onSave(statementText.trim())
                    }
                },
                enabled = statementText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack,
                    disabledContainerColor = DarkCharcoal,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_chief_aim_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Inscribe & Seal Aim",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Text(text = "Cancel", fontSize = 12.sp)
            }
        }
    )
}
