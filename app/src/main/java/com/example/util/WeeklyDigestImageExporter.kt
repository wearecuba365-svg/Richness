package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.UserProfileEntity
import com.example.data.model.WeeklyProgressDigest
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

object WeeklyDigestImageExporter {

    // High quality export dimensions for crisp display & social/archive sharing
    private const val CARD_WIDTH = 1080
    private const val CARD_HEIGHT = 1520

    // Luxury Dark & Gold Color Palette
    private val COLOR_DARK_BG = Color.rgb(18, 16, 14)
    private val COLOR_CARD_BG = Color.rgb(28, 25, 22)
    private val COLOR_STAT_BOX_BG = Color.rgb(36, 32, 28)
    private val COLOR_GOLD_PRIMARY = Color.rgb(212, 175, 55)
    private val COLOR_GOLD_LIGHT = Color.rgb(238, 212, 132)
    private val COLOR_GOLD_DARK = Color.rgb(166, 124, 30)
    private val COLOR_TEXT_PRIMARY = Color.rgb(245, 242, 235)
    private val COLOR_TEXT_SECONDARY = Color.rgb(188, 180, 168)
    private val COLOR_TEXT_MUTED = Color.rgb(138, 130, 118)
    private val COLOR_BORDER_GOLD = Color.rgb(186, 138, 38)
    private val COLOR_DIVIDER = Color.rgb(60, 54, 46)

    /**
     * Renders a WeeklyProgressDigest into a high-resolution dark/gold luxury image.
     */
    fun createDigestBitmap(digest: WeeklyProgressDigest, userProfile: UserProfileEntity?): Bitmap {
        val bitmap = Bitmap.createBitmap(CARD_WIDTH, CARD_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Base dark luxury background
        val bgPaint = Paint().apply {
            color = COLOR_DARK_BG
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, CARD_WIDTH.toFloat(), CARD_HEIGHT.toFloat(), bgPaint)

        // 2. Outer decorative gold gradient border
        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = COLOR_BORDER_GOLD
            isAntiAlias = true
        }
        val outerRect = RectF(36f, 36f, CARD_WIDTH - 36f, CARD_HEIGHT - 36f)
        canvas.drawRoundRect(outerRect, 24f, 24f, borderPaint)

        // Inner subtle border line
        val innerBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = COLOR_BORDER_GOLD
            alpha = 100
            isAntiAlias = true
        }
        val innerRect = RectF(48f, 48f, CARD_WIDTH - 48f, CARD_HEIGHT - 48f)
        canvas.drawRoundRect(innerRect, 18f, 18f, innerBorderPaint)

        // 3. Top Header: Brand Crest & Title
        val goldTitlePaint = Paint().apply {
            color = COLOR_GOLD_LIGHT
            textSize = 24f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            letterSpacing = 0.25f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("👑  THINK AND GROW RICH  👑", CARD_WIDTH / 2f, 115f, goldTitlePaint)

        val mainTitlePaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            textSize = 46f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            letterSpacing = 0.08f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("WEEKLY PROGRESS DIGEST", CARD_WIDTH / 2f, 175f, mainTitlePaint)

        val dateRangePaint = Paint().apply {
            color = COLOR_GOLD_PRIMARY
            textSize = 24f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val weekTitle = if (digest.isCurrentWeek) "Week ${digest.weekNumber} (Current) • ${digest.formattedDateRange}"
        else "Week ${digest.weekNumber} (${digest.weeksAgo}w ago) • ${digest.formattedDateRange}"
        canvas.drawText(weekTitle, CARD_WIDTH / 2f, 218f, dateRangePaint)

        // Divider
        val divPaint = Paint().apply {
            color = COLOR_DIVIDER
            strokeWidth = 1.5f
        }
        canvas.drawLine(80f, 248f, CARD_WIDTH - 80f, 248f, divPaint)

        // 4. Hero Plain-Language Summary Box
        val heroBoxRect = RectF(70f, 275f, CARD_WIDTH - 70f, 445f)
        val heroBoxBgPaint = Paint().apply {
            color = COLOR_CARD_BG
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawRoundRect(heroBoxRect, 18f, 18f, heroBoxBgPaint)

        val heroBorderPaint = Paint().apply {
            color = COLOR_BORDER_GOLD
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
            alpha = 180
            isAntiAlias = true
        }
        canvas.drawRoundRect(heroBoxRect, 18f, 18f, heroBorderPaint)

        // Performance Badge Chip
        val tagPaint = Paint().apply {
            color = COLOR_GOLD_LIGHT
            textSize = 20f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.1f
            isAntiAlias = true
        }
        canvas.drawText("✦  ${digest.performanceTierTag.uppercase(Locale.US)}", 100f, 320f, tagPaint)

        // Headline summary (word-wrapped)
        val headlinePaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            textSize = 28f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        drawWrappedText(
            canvas = canvas,
            text = digest.headlineSummary,
            x = 100f,
            y = 360f,
            maxWidth = (CARD_WIDTH - 200).toFloat(),
            lineHeight = 36f,
            paint = headlinePaint,
            maxLines = 2
        )

        // 5. Grid of 6 Key Numbers & Breakdowns
        val gridTop = 475f
        val colWidth = (CARD_WIDTH - 140 - 24) / 2f
        val rowHeight = 170f
        val gap = 20f

        val statItems = listOf(
            StatBoxData(
                icon = "📖",
                title = "JOURNAL & NOTES",
                mainValue = "${digest.journalEntriesCount} Entries",
                subtext = "${digest.decisionCount} Decisions • ${digest.fearReframeCount} Reframes",
                accentColor = COLOR_GOLD_PRIMARY
            ),
            StatBoxData(
                icon = "⚡",
                title = "HABITS & RITUALS",
                mainValue = "${digest.habitsCompletedCount} Done",
                subtext = "${digest.distinctHabitDays}/7 Active Days • ${(digest.habitCompletionRate * 100).toInt()}% Rate",
                accentColor = COLOR_GOLD_LIGHT
            ),
            StatBoxData(
                icon = "🔥",
                title = "SOVEREIGN STREAK",
                mainValue = "${digest.currentStreak} Days",
                subtext = "Best Streak: ${digest.bestStreak} Days",
                accentColor = Color.rgb(235, 120, 40)
            ),
            StatBoxData(
                icon = "✨",
                title = "XP GENERATED",
                mainValue = "+${digest.xpEarnedThisWeek} XP",
                subtext = "Tier: ${digest.currentTier}",
                accentColor = COLOR_GOLD_PRIMARY
            ),
            StatBoxData(
                icon = "🎯",
                title = "GOALS & WEALTH",
                mainValue = if (digest.wealthContributedAmount > 0) "+$${String.format(Locale.US, "%,.0f", digest.wealthContributedAmount)}" else "${digest.wealthContributionsCount} Actions",
                subtext = if (digest.mastermindCheckinSubmitted) "Circle Check-in: Inscribed ✓" else "Target Pace Active",
                accentColor = Color.rgb(76, 175, 80)
            ),
            StatBoxData(
                icon = "👑",
                title = "BADGES & RANK",
                mainValue = if (digest.badgesUnlockedThisWeek.isNotEmpty()) "${digest.badgesUnlockedThisWeek.size} New" else digest.currentTier,
                subtext = if (digest.badgesUnlockedThisWeek.isNotEmpty()) digest.badgesUnlockedThisWeek.first().title else "Mastery in Progress",
                accentColor = COLOR_GOLD_LIGHT
            )
        )

        for (i in statItems.indices) {
            val row = i / 2
            val col = i % 2
            val x = 70f + col * (colWidth + gap)
            val y = gridTop + row * (rowHeight + gap)
            drawStatBox(canvas, statItems[i], RectF(x, y, x + colWidth, y + rowHeight))
        }

        // 6. Napoleon Hill Weekly Law Card
        val quoteBoxTop = gridTop + 3 * (rowHeight + gap) + 15f
        val quoteBoxRect = RectF(70f, quoteBoxTop, CARD_WIDTH - 70f, quoteBoxTop + 190f)

        canvas.drawRoundRect(quoteBoxRect, 16f, 16f, heroBoxBgPaint)
        canvas.drawRoundRect(quoteBoxRect, 16f, 16f, heroBorderPaint)

        val quoteTitlePaint = Paint().apply {
            color = COLOR_GOLD_PRIMARY
            textSize = 19f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.1f
            isAntiAlias = true
        }
        canvas.drawText("LAW OF PERSISTENCE & ACCUMULATION", 100f, quoteBoxTop + 45f, quoteTitlePaint)

        val quoteBodyPaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            textSize = 22f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            isAntiAlias = true
        }
        drawWrappedText(
            canvas = canvas,
            text = "\"Riches do not respond to wishful thinking. They respond only to definite plans, backed by definite desires, through constant PERSISTENCE.\"",
            x = 100f,
            y = quoteBoxTop + 85f,
            maxWidth = (CARD_WIDTH - 200).toFloat(),
            lineHeight = 32f,
            paint = quoteBodyPaint,
            maxLines = 3
        )

        // 7. Footer: Member Signature & App Signature
        val footerY = CARD_HEIGHT - 90f
        val userName = userProfile?.name ?: "Sovereign Member"
        val memberPaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            textSize = 21f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
        canvas.drawText("Member: $userName (${userProfile?.tierName ?: "Novice"} Tier)", 70f, footerY, memberPaint)

        val appSignPaint = Paint().apply {
            color = COLOR_TEXT_MUTED
            textSize = 19f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = Paint.Align.RIGHT
            isAntiAlias = true
        }
        canvas.drawText("Think and Grow Rich Applet", CARD_WIDTH - 70f, footerY, appSignPaint)

        return bitmap
    }

    private data class StatBoxData(
        val icon: String,
        val title: String,
        val mainValue: String,
        val subtext: String,
        val accentColor: Int
    )

    private fun drawStatBox(canvas: Canvas, data: StatBoxData, rect: RectF) {
        val bgPaint = Paint().apply {
            color = COLOR_STAT_BOX_BG
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawRoundRect(rect, 14f, 14f, bgPaint)

        val borderPaint = Paint().apply {
            color = COLOR_DIVIDER
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }
        canvas.drawRoundRect(rect, 14f, 14f, borderPaint)

        // Top Row: Icon + Title
        val titlePaint = Paint().apply {
            color = COLOR_TEXT_MUTED
            textSize = 18f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.08f
            isAntiAlias = true
        }
        canvas.drawText("${data.icon}  ${data.title}", rect.left + 22f, rect.top + 38f, titlePaint)

        // Main Value
        val valPaint = Paint().apply {
            color = data.accentColor
            textSize = 34f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(data.mainValue, rect.left + 22f, rect.top + 92f, valPaint)

        // Subtext
        val subPaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            textSize = 19f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
        }
        canvas.drawText(data.subtext, rect.left + 22f, rect.top + 134f, subPaint)
    }

    private fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Float,
        lineHeight: Float,
        paint: Paint,
        maxLines: Int
    ) {
        val words = text.split(" ")
        var currentLine = ""
        var currentY = y
        var linesCount = 0

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val width = paint.measureText(testLine)
            if (width > maxWidth && currentLine.isNotEmpty()) {
                canvas.drawText(currentLine, x, currentY, paint)
                currentLine = word
                currentY += lineHeight
                linesCount++
                if (linesCount >= maxLines - 1) {
                    break
                }
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty() && linesCount < maxLines) {
            canvas.drawText(currentLine, x, currentY, paint)
        }
    }

    /**
     * Saves the rendered bitmap to cache and launches a share intent.
     */
    fun exportAndShareDigestImage(
        context: Context,
        digest: WeeklyProgressDigest,
        userProfile: UserProfileEntity?
    ): Result<File> {
        return runCatching {
            val bitmap = createDigestBitmap(digest, userProfile)
            val exportDir = File(context.cacheDir, "digest_exports").apply { mkdirs() }
            val fileName = "Weekly_Digest_Week${digest.weekNumber}_${digest.year}.png"
            val file = File(exportDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareText = """
                👑 Think and Grow Rich • Weekly Progress Digest
                Week ${digest.weekNumber} (${digest.formattedDateRange})
                
                ${digest.headlineSummary}
                
                • Journal Entries: ${digest.journalEntriesCount}
                • Habits Completed: ${digest.habitsCompletedCount} (${digest.distinctHabitDays}/7 days)
                • Sovereign Streak: ${digest.currentStreak} Days
                • XP Generated: +${digest.xpEarnedThisWeek} XP
                • Sovereign Tier: ${digest.currentTier}
                
                #ThinkAndGrowRich #SovereignRitual #NapoleonHill
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, shareText)
                putExtra(Intent.EXTRA_SUBJECT, "My Think and Grow Rich Weekly Digest (Week ${digest.weekNumber})")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share Weekly Progress Digest").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)

            file
        }
    }
}
