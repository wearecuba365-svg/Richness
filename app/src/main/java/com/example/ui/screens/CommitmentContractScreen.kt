package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommitmentContractEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.CreateCommitmentContractDialog
import com.example.ui.components.RenewCommitmentContractDialog
import com.example.ui.components.CompleteCommitmentContractDialog
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
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.viewmodel.RichesViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommitmentContractScreen(
    viewModel: RichesViewModel,
    onBack: () -> Unit,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val activeContract by viewModel.activeCommitmentContract.collectAsState()
    val allContracts by viewModel.allCommitmentContracts.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val showCreateDialog by viewModel.showCreateCommitmentDialog.collectAsState()
    val showRenewDialog by viewModel.showRenewCommitmentDialog.collectAsState()
    val showCompleteDialog by viewModel.showCompleteCommitmentDialog.collectAsState()

    var selectedTab by remember { mutableIntStateOf(initialTab) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📜", fontSize = 18.sp)
                        Text(
                            text = "Commitment Contract",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextPrimary else RichBlack
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("commitment_screen_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDark) TextPrimary else RichBlack
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.openCreateCommitmentDialog() },
                        modifier = Modifier.testTag("commitment_screen_add_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Contract",
                            tint = GoldPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) RichBlack else Color.White
                )
            )
        },
        containerColor = if (isDark) RichBlack else Color(0xFFF8FAFC),
        modifier = modifier.testTag("commitment_contract_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = if (isDark) SurfaceElevated else Color.White,
                contentColor = GoldPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = GoldPrimary,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Active Covenant",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) GoldPrimary else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("commitment_tab_active")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "My Commitments",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) GoldPrimary else TextSecondary
                            )
                            if (allContracts.isNotEmpty()) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (selectedTab == 1) GoldPrimary else DarkBorder,
                                    modifier = Modifier.size(18.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${allContracts.size}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedTab == 1) RichBlack else TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.testTag("commitment_tab_history")
                )
            }

            // Tab Content
            if (selectedTab == 0) {
                ActiveCovenantTabContent(
                    contract = activeContract,
                    userProfile = userProfile,
                    onCreateClick = { viewModel.openCreateCommitmentDialog() },
                    onUpdateProgress = { id, p -> viewModel.updateCommitmentProgress(id, p) },
                    onCompleteClick = { viewModel.openCompleteCommitmentDialog(it) },
                    onRenewClick = { viewModel.openRenewCommitmentDialog(it) },
                    onDeleteClick = { viewModel.deleteCommitmentContract(it.id) }
                )
            } else {
                CommitmentHistoryTabContent(
                    contracts = allContracts,
                    onCreateClick = { viewModel.openCreateCommitmentDialog() },
                    onCompleteClick = { viewModel.openCompleteCommitmentDialog(it) },
                    onRenewClick = { viewModel.openRenewCommitmentDialog(it) },
                    onDeleteClick = { viewModel.deleteCommitmentContract(it.id) }
                )
            }
        }
    }

    // Modal Dialogs
    if (showCreateDialog) {
        CreateCommitmentContractDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.closeCreateCommitmentDialog() },
            onConfirm = { goal, why, deadline, sign ->
                viewModel.createCommitmentContract(goal, why, deadline, sign)
            }
        )
    }

    showRenewDialog?.let { contractToRenew ->
        RenewCommitmentContractDialog(
            contract = contractToRenew,
            onDismiss = { viewModel.closeRenewCommitmentDialog() },
            onConfirm = { newDeadline, notes ->
                viewModel.renewCommitmentContract(contractToRenew.id, newDeadline, notes)
            }
        )
    }

    showCompleteDialog?.let { contractToComplete ->
        CompleteCommitmentContractDialog(
            contract = contractToComplete,
            onDismiss = { viewModel.closeCompleteCommitmentDialog() },
            onConfirm = { notes ->
                viewModel.completeCommitmentContract(contractToComplete.id, notes)
            }
        )
    }
}

@Composable
private fun ActiveCovenantTabContent(
    contract: CommitmentContractEntity?,
    userProfile: com.example.data.model.UserProfileEntity?,
    onCreateClick: () -> Unit,
    onUpdateProgress: (Long, Int) -> Unit,
    onCompleteClick: (CommitmentContractEntity) -> Unit,
    onRenewClick: (CommitmentContractEntity) -> Unit,
    onDeleteClick: (CommitmentContractEntity) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val now = remember { System.currentTimeMillis() }

    if (contract == null) {
        // Empty State: Prompt user to draft a commitment
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary.copy(alpha = 0.15f))
                        .border(1.5.dp, GoldPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📜", fontSize = 36.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No Active Covenant Sealed",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextPrimary else RichBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Transmute your Definite Chief Aim into an irrevocable formal contract. Set a clear deadline date, declare why it matters, and sign your covenant with unwavering resolve.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                val chiefAim = userProfile?.definiteChiefAim?.trim().orEmpty()
                if (chiefAim.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) SurfaceElevated else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "YOUR CURRENT CHIEF AIM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "“$chiefAim”",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif,
                                color = if (isDark) TextPrimary else RichBlack,
                                lineHeight = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Button(
                    onClick = onCreateClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .testTag("empty_screen_draft_contract_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Draft Commitment Contract (+75 XP)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    } else {
        // Render Majestic Formal Parchment Contract Document
        val isDeadlineReached = contract.isDeadlineReached(now)
        val daysRemaining = contract.getDaysRemaining(now)

        var sliderProgress by remember(contract.id, contract.progressPercent) {
            mutableFloatStateOf(contract.progressPercent.toFloat())
        }

        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseAlpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_alpha"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // If deadline reached: Prominent top resolution banner
            if (isDeadlineReached) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AmberAccent.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, AmberBright.copy(alpha = pulseAlpha)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = AmberBright,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "TARGET DEADLINE REACHED",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AmberBright,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "The target date for this covenant has arrived. Affirm your victory or recommit by extending the deadline.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onCompleteClick(contract) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = RichBlack
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Complete (+150 XP)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                OutlinedButton(
                                    onClick = { onRenewClick(contract) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = GoldLight
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Autorenew,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Renew (+50 XP)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // The Formal Parchment Document Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) SurfaceElevated else Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (isDeadlineReached) AmberBright.copy(alpha = pulseAlpha) else GoldPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_formal_contract_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ornate Header Crest
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "⚜️", fontSize = 16.sp)
                            Text(
                                text = "SOVEREIGN COVENANT",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                letterSpacing = 2.sp
                            )
                            Text(text = "⚜️", fontSize = 16.sp)
                        }

                        Text(
                            text = "DECREE OF DEFINITENESS OF PURPOSE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = GoldPrimary.copy(alpha = 0.3f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Signature addressee
                        Text(
                            text = "Issued Unto & Sealed By: ${contract.signatureName.ifBlank { "Sovereign Initiate" }}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // The Goal Statement in large serif quote
                        Text(
                            text = "“${contract.goalStatement}”",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextPrimary else RichBlack,
                            textAlign = TextAlign.Center,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Sacred Purpose section
                        if (contract.whyItMatters.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isDark) DarkCharcoal.copy(alpha = 0.5f) else Color(0xFFF1F5F9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "SACRED PURPOSE & STAKES (WHY IT MATTERS)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary,
                                        letterSpacing = 0.8.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = contract.whyItMatters,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Target Deadline & Countdown Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TARGET DEADLINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                                Text(
                                    text = contract.getFormattedDeadline(),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) TextPrimary else RichBlack
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isDeadlineReached) AmberAccent.copy(alpha = 0.25f) else GoldDark.copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isDeadlineReached) AmberBright else GoldPrimary.copy(alpha = 0.6f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isDeadlineReached) Icons.Default.Warning else Icons.Default.HourglassBottom,
                                        contentDescription = null,
                                        tint = if (isDeadlineReached) AmberBright else GoldLight,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (isDeadlineReached) "DEADLINE REACHED" else {
                                            if (daysRemaining == 0L) "DUE TODAY" else "$daysRemaining DAYS LEFT"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isDeadlineReached) AmberBright else GoldLight
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Progress Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MANIFESTATION PROGRESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "${sliderProgress.toInt()}%",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Slider(
                            value = sliderProgress,
                            onValueChange = { sliderProgress = it },
                            valueRange = 0f..100f,
                            steps = 19,
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = if (isDark) DarkCharcoal else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Quick milestone stepper buttons (25%, 50%, 75%, 100%)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(25, 50, 75, 100).forEach { milestone ->
                                OutlinedButton(
                                    onClick = {
                                        sliderProgress = milestone.toFloat()
                                        onUpdateProgress(contract.id, milestone)
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = if (sliderProgress.toInt() >= milestone) GoldPrimary else TextSecondary
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (sliderProgress.toInt() >= milestone) GoldPrimary.copy(alpha = 0.6f) else DarkBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(30.dp)
                                ) {
                                    Text("$milestone%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (sliderProgress.toInt() != contract.progressPercent) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { onUpdateProgress(contract.id, sliderProgress.toInt()) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                            ) {
                                Text("Save Progress to Contract", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Formal Signature Seal Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isDark) DarkCharcoal.copy(alpha = 0.4f) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.radialGradient(
                                                    listOf(GoldLight, GoldDark)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "⚜️", fontSize = 16.sp)
                                    }

                                    Column {
                                        Text(
                                            text = "SOLEMN SEAL & SIGNATURE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldPrimary
                                        )
                                        Text(
                                            text = contract.signatureName.ifBlank { "Sovereign Initiate" },
                                            fontSize = 13.sp,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) TextPrimary else RichBlack
                                        )
                                    }
                                }

                                Text(
                                    text = contract.getFormattedSignedDate(),
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Primary Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onCompleteClick(contract) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(44.dp)
                                    .testTag("contract_screen_complete_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Complete (+150 XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            OutlinedButton(
                                onClick = { onRenewClick(contract) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = GoldLight
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("contract_screen_renew_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Renew (+50 XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { onDeleteClick(contract) },
                                modifier = Modifier
                                    .size(44.dp)
                                    .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Contract",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommitmentHistoryTabContent(
    contracts: List<CommitmentContractEntity>,
    onCreateClick: () -> Unit,
    onCompleteClick: (CommitmentContractEntity) -> Unit,
    onRenewClick: (CommitmentContractEntity) -> Unit,
    onDeleteClick: (CommitmentContractEntity) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val completedCount = contracts.count { it.status == CommitmentContractEntity.STATUS_COMPLETED }
    val renewedCount = contracts.count { it.status == CommitmentContractEntity.STATUS_RENEWED }
    val activeCount = contracts.count { it.status == CommitmentContractEntity.STATUS_ACTIVE }
    val totalXpAwarded = contracts.sumOf {
        it.xpAwardedForCreation + if (it.status == CommitmentContractEntity.STATUS_COMPLETED) it.xpAwardedForCompletion else 0
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary KPI Bar
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) SurfaceElevated else Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KpiStatItem(label = "Total Sealed", value = "${contracts.size}")
                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = DarkBorder
                    )
                    KpiStatItem(label = "Completed 🏆", value = "$completedCount", valueColor = GoldPrimary)
                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = DarkBorder
                    )
                    KpiStatItem(label = "Renewed 🔄", value = "$renewedCount")
                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = DarkBorder
                    )
                    KpiStatItem(label = "XP Earned", value = "+$totalXpAwarded", valueColor = GoldLight)
                }
            }
        }

        if (contracts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📜", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "No Past Commitments Yet",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) TextPrimary else RichBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Every covenant you seal and fulfill is permanently etched here into your sovereign legacy.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onCreateClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Draft First Commitment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(contracts, key = { it.id }) { contract ->
                PastCommitmentCard(
                    contract = contract,
                    onCompleteClick = { onCompleteClick(contract) },
                    onRenewClick = { onRenewClick(contract) },
                    onDeleteClick = { onDeleteClick(contract) }
                )
            }
        }
    }
}

@Composable
private fun PastCommitmentCard(
    contract: CommitmentContractEntity,
    onCompleteClick: () -> Unit,
    onRenewClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val statusColor = when (contract.status) {
        CommitmentContractEntity.STATUS_COMPLETED -> GoldPrimary
        CommitmentContractEntity.STATUS_RENEWED -> AmberBright
        else -> GoldLight
    }

    val statusIcon = when (contract.status) {
        CommitmentContractEntity.STATUS_COMPLETED -> Icons.Default.EmojiEvents
        CommitmentContractEntity.STATUS_RENEWED -> Icons.Default.Autorenew
        else -> Icons.Default.HourglassBottom
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) SurfaceElevated else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (contract.status == CommitmentContractEntity.STATUS_COMPLETED) GoldPrimary.copy(alpha = 0.5f) else DarkBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = contract.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColor
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Signed: ${contract.getFormattedSignedDate()}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "“${contract.goalStatement}”",
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = if (isDark) TextPrimary else RichBlack,
                lineHeight = 20.sp
            )

            if (contract.whyItMatters.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Purpose: ${contract.whyItMatters}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // If completed or renewed, show notes
            if (contract.status == CommitmentContractEntity.STATUS_COMPLETED && contract.completionNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldDark.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "👑 Victory Reflection:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Text(
                            text = contract.completionNotes,
                            fontSize = 11.sp,
                            color = if (isDark) TextPrimary else RichBlack
                        )
                    }
                }
            } else if (contract.status == CommitmentContractEntity.STATUS_RENEWED && contract.renewalNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AmberAccent.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberBright.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "🔄 Strategic Renewal Reflection:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberBright
                        )
                        Text(
                            text = contract.renewalNotes,
                            fontSize = 11.sp,
                            color = if (isDark) TextPrimary else RichBlack
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ${contract.getFormattedDeadline()}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Final Progress: ${contract.progressPercent}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            }
        }
    }
}

@Composable
private fun KpiStatItem(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    val isDark = LocalIsDarkTheme.current
    val finalColor = if (valueColor != Color.Unspecified) valueColor else if (isDark) TextPrimary else RichBlack

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = finalColor
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = TextSecondary
        )
    }
}
