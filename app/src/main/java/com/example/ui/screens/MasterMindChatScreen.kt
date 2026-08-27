package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.data.remote.gemini.AdvisorRole
import com.example.ui.components.LocalWindowSizeInfo
import com.example.data.remote.gemini.ChatMessage
import com.example.data.remote.gemini.ChatSender
import com.example.data.remote.gemini.GeminiModelChoice
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.BrushedMetal
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MasterMindChatScreen(
    messages: List<ChatMessage>,
    selectedRole: AdvisorRole,
    selectedModel: GeminiModelChoice,
    isLoading: Boolean,
    userProfile: UserProfileEntity?,
    onSelectRole: (AdvisorRole) -> Unit,
    onSelectModel: (GeminiModelChoice) -> Unit,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onSaveToNotebook: (title: String, content: String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    // Auto-scroll to latest message
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val promptSuggestions = listOf(
        "Formulate my Definite Major Purpose decree",
        "How do I transmute temporary defeat into gold?",
        "Prescribe a Master Mind routine for my tier",
        "Explain the 6th sense and creative imagination"
    )

    val windowInfo = LocalWindowSizeInfo.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .imePadding()
            .testTag("mastermind_chat_screen")
    ) {
        // --- Header Banner ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E1A14), DarkCharcoal)
                    )
                )
                .border(width = 0.5.dp, color = DarkBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
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
                            .background(
                                Brush.linearGradient(listOf(GoldDark, GoldLight))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "AI Council",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "The Master Mind AI Council",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = GoldLight
                        )
                        Text(
                            text = "${selectedRole.displayName} • ${selectedModel.displayName}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                IconButton(
                    onClick = onClearChat,
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteSweep,
                        contentDescription = "Clear Chat History",
                        tint = TextMuted
                    )
                }
            }
        }

        if (windowInfo.isTabletOrFoldable) {
            // --- TABLET / EXPANDED 2-PANE LAYOUT ---
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // LEFT PANE: Council Mentors & Settings
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF131317))
                        .border(width = 0.5.dp, color = DarkBorder)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "COUNCIL MENTORS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        AdvisorRole.values().forEach { role ->
                            val isSelected = role == selectedRole
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectRole(role) }
                                    .border(
                                        width = if (isSelected) 1.dp else 0.5.dp,
                                        color = if (isSelected) GoldPrimary else DarkBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .testTag("role_chip_${role.name.lowercase()}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) GoldPrimary.copy(alpha = 0.15f) else SurfaceElevated
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) AmberBright else TextMuted)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = role.displayName,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) GoldLight else TextPrimary
                                        )
                                        Text(
                                            text = role.subtitle,
                                            fontSize = 10.sp,
                                            color = TextMuted,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "INTELLIGENCE ENGINE",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GeminiModelChoice.values().forEach { model ->
                            val isSelected = model == selectedModel
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) GoldPrimary.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        0.5.dp,
                                        if (isSelected) GoldPrimary else DarkBorder,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { onSelectModel(model) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = model.displayName,
                                    fontSize = 10.5.sp,
                                    color = if (isSelected) GoldLight else TextMuted,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "RAPID INITIATIONS",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    promptSuggestions.take(3).forEach { prompt ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .border(0.5.dp, DarkBorder, RoundedCornerShape(6.dp))
                                .clickable { onSendMessage(prompt) }
                        ) {
                            Text(
                                text = prompt,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // RIGHT PANE: Chat Conversation and Input
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (messages.isEmpty()) {
                            ChatEmptyState(
                                userProfile = userProfile,
                                promptSuggestions = promptSuggestions,
                                onSendMessage = onSendMessage
                            )
                        } else {
                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(messages, key = { it.id }) { msg ->
                                    ChatMessageItem(
                                        message = msg,
                                        onSaveToNotebook = {
                                            onSaveToNotebook("Master Mind Insight (${msg.advisorRole.displayName})", msg.text)
                                            Toast.makeText(context, "Saved to Sovereign Notebook", Toast.LENGTH_SHORT).show()
                                        },
                                        onCopy = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("MasterMind AI", msg.text)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                                if (isLoading) {
                                    item {
                                        TypingIndicatorItem(role = selectedRole)
                                    }
                                }
                            }
                        }
                    }

                    ChatBottomInputBar(
                        inputText = inputText,
                        onInputTextChange = { inputText = it },
                        selectedRole = selectedRole,
                        isLoading = isLoading,
                        onSendMessage = {
                            if (inputText.isNotBlank() && !isLoading) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                }
            }
        } else {
            // --- COMPACT PHONE SINGLE-COLUMN LAYOUT ---
            // --- Advisor Roles Bar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131317))
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AdvisorRole.values().forEach { role ->
                    val isSelected = role == selectedRole
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectRole(role) },
                        label = {
                            Text(
                                text = role.displayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary.copy(alpha = 0.2f),
                            selectedLabelColor = GoldLight,
                            containerColor = SurfaceElevated,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = DarkBorder,
                            selectedBorderColor = GoldPrimary
                        ),
                        modifier = Modifier.testTag("role_chip_${role.name.lowercase()}")
                    )
                }
            }

            // --- Model Selector Bar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F0F14))
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Model:", fontSize = 10.sp, color = TextMuted)
                }

                GeminiModelChoice.values().forEach { model ->
                    val isSelected = model == selectedModel
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) GoldPrimary.copy(alpha = 0.15f) else Color.Transparent)
                            .border(
                                0.5.dp,
                                if (isSelected) GoldPrimary else DarkBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onSelectModel(model) }
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .testTag("model_selector_${model.name.lowercase()}")
                    ) {
                        Text(
                            text = "${model.displayName} (${model.tag})",
                            fontSize = 10.sp,
                            color = if (isSelected) GoldLight else TextMuted,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            // --- Messages Thread ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (messages.isEmpty()) {
                    ChatEmptyState(
                        userProfile = userProfile,
                        promptSuggestions = promptSuggestions,
                        onSendMessage = onSendMessage
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(messages, key = { it.id }) { msg ->
                            ChatMessageItem(
                                message = msg,
                                onSaveToNotebook = {
                                    onSaveToNotebook("Master Mind Insight (${msg.advisorRole.displayName})", msg.text)
                                    Toast.makeText(context, "Saved to Sovereign Notebook", Toast.LENGTH_SHORT).show()
                                },
                                onCopy = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("MasterMind AI", msg.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        if (isLoading) {
                            item {
                                TypingIndicatorItem(role = selectedRole)
                            }
                        }
                    }
                }
            }

            // --- Bottom Input Box ---
            ChatBottomInputBar(
                inputText = inputText,
                onInputTextChange = { inputText = it },
                selectedRole = selectedRole,
                isLoading = isLoading,
                onSendMessage = {
                    if (inputText.isNotBlank() && !isLoading) {
                        onSendMessage(inputText)
                        inputText = ""
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatEmptyState(
    userProfile: UserProfileEntity?,
    promptSuggestions: List<String>,
    onSendMessage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.1f))
                .border(1.dp, GoldPrimary.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Commune with the Master Mind",
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = GoldLight
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your identity is set as ${userProfile?.tierName ?: "Novice"} (Score: ${userProfile?.mindsetScore ?: 50}/100). Ask deep questions on purpose, autosuggestion, and capital alchemy.",
            fontSize = 12.sp,
            color = TextMuted,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SUGGESTED INITIATIONS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AmberAccent,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        promptSuggestions.forEach { prompt ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(0.5.dp, DarkBorder, RoundedCornerShape(10.dp))
                    .clickable { onSendMessage(prompt) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = prompt,
                        fontSize = 12.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBottomInputBar(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    selectedRole: AdvisorRole,
    isLoading: Boolean,
    onSendMessage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131317))
            .border(0.5.dp, DarkBorder)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputTextChange,
                placeholder = {
                    Text(
                        "Consult ${selectedRole.displayName}...",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkCharcoal,
                    unfocusedContainerColor = DarkCharcoal
                ),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = { onSendMessage() }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (inputText.isNotBlank() && !isLoading)
                            Brush.linearGradient(listOf(GoldDark, GoldLight))
                        else
                            Brush.linearGradient(listOf(Color(0xFF2A2A32), Color(0xFF222228)))
                    )
                    .clickable(enabled = inputText.isNotBlank() && !isLoading) {
                        onSendMessage()
                    }
                    .testTag("chat_send_button"),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = GoldLight,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) RichBlack else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    onSaveToNotebook: () -> Unit,
    onCopy: () -> Unit
) {
    val isUser = message.sender == ChatSender.USER
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(GoldDark, GoldPrimary))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = RichBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            // Header tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = if (isUser) "You" else message.advisorRole.displayName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) TextMuted else GoldLight
                )
                if (!isUser && message.modelUsed != null) {
                    Text(
                        text = " • ${message.modelUsed}",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formattedTime,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }

            // Message Bubble
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isUser) 14.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 14.dp
                        )
                    )
                    .background(
                        if (isUser) Color(0xFF22222C)
                        else Color(0xFF18171E)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isUser) DarkBorder else GoldPrimary.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (isUser) 14.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 14.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = if (isUser) TextPrimary else Color(0xFFE8E5DD)
                )
            }

            // AI Action Buttons
            if (!isUser) {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceElevated)
                            .clickable { onSaveToNotebook() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkAdd,
                                contentDescription = "Save to Notebook",
                                tint = AmberAccent,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save to Journal", fontSize = 10.sp, color = GoldLight)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceElevated)
                            .clickable { onCopy() }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copy",
                                tint = TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingIndicatorItem(role: AdvisorRole) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF18171E))
                .border(0.5.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${role.displayName} is formulating counsel",
                    fontSize = 12.sp,
                    color = GoldLight,
                    modifier = Modifier.alpha(alpha)
                )
                Spacer(modifier = Modifier.width(4.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    color = GoldLight,
                    strokeWidth = 1.5.dp
                )
            }
        }
    }
}
