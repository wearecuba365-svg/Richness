package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PdfExportResult(
    val file: File,
    val uri: Uri,
    val totalEntries: Int,
    val pageCount: Int,
    val filePath: String
)

object NotebookPdfExporter {

    // A4 Dimensions in Points (72 dpi): 595 x 842 pt
    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842

    private const val MARGIN_LEFT = 42f
    private const val MARGIN_RIGHT = 42f
    private const val MARGIN_TOP = 46f
    private const val MARGIN_BOTTOM = 52f
    private const val CONTENT_WIDTH = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT

    // Sovereign Luxury Color Palette for Print & Digital Reading
    private val COLOR_OBSIDIAN = Color.rgb(22, 20, 18)
    private val COLOR_CHARCOAL = Color.rgb(52, 48, 44)
    private val COLOR_MUTED = Color.rgb(118, 112, 104)
    private val COLOR_GOLD_DARK = Color.rgb(154, 114, 28)
    private val COLOR_GOLD_PRIMARY = Color.rgb(186, 138, 38)
    private val COLOR_GOLD_LIGHT = Color.rgb(224, 186, 92)
    private val COLOR_GOLD_FAINT = Color.rgb(248, 243, 230)
    private val COLOR_IVORY_BG = Color.rgb(254, 252, 248)
    private val COLOR_CARD_BG = Color.rgb(250, 247, 240)
    private val COLOR_CARD_BORDER = Color.rgb(226, 218, 204)
    private val COLOR_DIVIDER = Color.rgb(212, 196, 168)

    /**
     * Generates a stylized, typography-focused PDF containing all provided notebook entries.
     */
    fun exportNotebookToPdf(
        context: Context,
        entries: List<NotebookEntryEntity>,
        userProfile: UserProfileEntity?,
        customTitle: String = "THE SOVEREIGN RITUAL NOTEBOOK"
    ): Result<PdfExportResult> {
        return runCatching {
            val pdfDocument = PdfDocument()
            try {
                // Setup reusable paints
                val backgroundPaint = Paint().apply {
                    color = COLOR_IVORY_BG
                    style = Paint.Style.FILL
                }

                val goldRulePaint = Paint().apply {
                    color = COLOR_GOLD_PRIMARY
                    style = Paint.Style.STROKE
                    strokeWidth = 1.2f
                    isAntiAlias = true
                }

                val thinRulePaint = Paint().apply {
                    color = COLOR_DIVIDER
                    style = Paint.Style.STROKE
                    strokeWidth = 0.6f
                    isAntiAlias = true
                }

                val titlePaint = TextPaint().apply {
                    color = COLOR_OBSIDIAN
                    textSize = 15f
                    typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
                    isAntiAlias = true
                }

                val promptPaint = TextPaint().apply {
                    color = COLOR_GOLD_DARK
                    textSize = 10f
                    typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
                    isAntiAlias = true
                }

                val bodyPaint = TextPaint().apply {
                    color = COLOR_CHARCOAL
                    textSize = 10.5f
                    typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
                    isAntiAlias = true
                }

                val metaPaint = TextPaint().apply {
                    color = COLOR_MUTED
                    textSize = 8.5f
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                    isAntiAlias = true
                }

                val metaBoldPaint = TextPaint().apply {
                    color = COLOR_GOLD_DARK
                    textSize = 8.5f
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    isAntiAlias = true
                }

                var currentPageNumber = 1
                var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNumber).create()
                var page = pdfDocument.startPage(pageInfo)
                var canvas = page.canvas

                // Fill page 1 background
                canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), backgroundPaint)

                var currentY = MARGIN_TOP

                // Draw Cover & Header on Page 1
                currentY = drawDocumentHeader(
                    canvas = canvas,
                    userProfile = userProfile,
                    entriesCount = entries.size,
                    customTitle = customTitle,
                    goldRulePaint = goldRulePaint,
                    thinRulePaint = thinRulePaint,
                    metaPaint = metaPaint,
                    startY = currentY
                )

                // Draw each entry
                val dateFormat = SimpleDateFormat("MMMM dd, yyyy • hh:mm a", Locale.getDefault())
                val sortedEntries = entries.sortedByDescending { it.timestamp }

                for ((index, entry) in sortedEntries.withIndex()) {
                    val entryNumber = sortedEntries.size - index

                    // Measure entry height before drawing
                    val titleLayout = createStaticLayout(entry.title, titlePaint, (CONTENT_WIDTH - 28f).toInt())
                    val promptLayout = if (!entry.promptQuestion.isNullOrBlank()) {
                        createStaticLayout("Prompt: ${entry.promptQuestion}", promptPaint, (CONTENT_WIDTH - 36f).toInt())
                    } else null
                    val bodyLayout = createStaticLayout(entry.content, bodyPaint, (CONTENT_WIDTH - 28f).toInt())

                    val estimatedCardHeight = 44f +
                            titleLayout.height +
                            (if (promptLayout != null) promptLayout.height + 12f else 0f) +
                            bodyLayout.height +
                            26f

                    // Check if card fits on current page
                    if (currentY + estimatedCardHeight > (PAGE_HEIGHT - MARGIN_BOTTOM - 20f)) {
                        // Draw footer on current page
                        drawPageFooter(canvas, currentPageNumber, thinRulePaint, metaPaint)
                        pdfDocument.finishPage(page)

                        // Start new page
                        currentPageNumber++
                        pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, currentPageNumber).create()
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        canvas.drawRect(0f, 0f, PAGE_WIDTH.toFloat(), PAGE_HEIGHT.toFloat(), backgroundPaint)

                        // Draw running header on new page
                        currentY = drawRunningHeader(canvas, currentPageNumber, goldRulePaint, metaPaint)
                    }

                    // Render entry card
                    currentY = drawEntryCard(
                        canvas = canvas,
                        entry = entry,
                        entryNumber = entryNumber,
                        formattedDate = dateFormat.format(Date(entry.timestamp)),
                        titleLayout = titleLayout,
                        promptLayout = promptLayout,
                        bodyLayout = bodyLayout,
                        startY = currentY,
                        metaPaint = metaPaint,
                        metaBoldPaint = metaBoldPaint,
                        titlePaint = titlePaint,
                        promptPaint = promptPaint,
                        bodyPaint = bodyPaint
                    )

                    currentY += 14f // Spacing between cards
                }

                // Draw footer on final page
                drawPageFooter(canvas, currentPageNumber, thinRulePaint, metaPaint)
                pdfDocument.finishPage(page)

                // Save PDF to cache dir
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val exportDir = File(context.cacheDir, "pdf_exports").apply { mkdirs() }
                val file = File(exportDir, "Sovereign_Notebook_Archive_$timestamp.pdf")

                FileOutputStream(file).use { outStream ->
                    pdfDocument.writeTo(outStream)
                }

                val authority = "${context.packageName}.fileprovider"
                val uri: Uri = runCatching {
                    FileProvider.getUriForFile(context, authority, file)
                }.getOrElse {
                    Uri.fromFile(file)
                }

                PdfExportResult(
                    file = file,
                    uri = uri,
                    totalEntries = entries.size,
                    pageCount = currentPageNumber,
                    filePath = file.absolutePath
                )
            } finally {
                pdfDocument.close()
            }
        }
    }

    private fun drawDocumentHeader(
        canvas: Canvas,
        userProfile: UserProfileEntity?,
        entriesCount: Int,
        customTitle: String,
        goldRulePaint: Paint,
        thinRulePaint: Paint,
        metaPaint: TextPaint,
        startY: Float
    ): Float {
        var y = startY

        // Double Gold Crest Border at Top
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + CONTENT_WIDTH, y, goldRulePaint)
        y += 2.5f
        canvas.drawLine(MARGIN_LEFT + 20f, y, MARGIN_LEFT + CONTENT_WIDTH - 20f, y, thinRulePaint)
        y += 18f

        // Brand Sub-heading
        val superTitlePaint = TextPaint().apply {
            color = COLOR_GOLD_DARK
            textSize = 9.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.15f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("THE RICHES PROTOCOL • SOVEREIGN COMPENDIUM", PAGE_WIDTH / 2f, y, superTitlePaint)
        y += 18f

        // Main Document Title (Large Serif Display)
        val mainTitlePaint = TextPaint().apply {
            color = COLOR_OBSIDIAN
            textSize = 19f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            letterSpacing = 0.04f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(customTitle, PAGE_WIDTH / 2f, y, mainTitlePaint)
        y += 14f

        val subtitlePaint = TextPaint().apply {
            color = COLOR_CHARCOAL
            textSize = 10f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("A Permanent Record of Transmutations, Affirmations & Definiteness of Purpose", PAGE_WIDTH / 2f, y, subtitlePaint)
        y += 16f

        // Metadata Telemetry Box
        val metaBoxHeight = 36f
        val metaBoxRect = RectF(MARGIN_LEFT, y, MARGIN_LEFT + CONTENT_WIDTH, y + metaBoxHeight)
        val metaBgPaint = Paint().apply {
            color = COLOR_CARD_BG
            style = Paint.Style.FILL
        }
        val metaBorderPaint = Paint().apply {
            color = COLOR_CARD_BORDER
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
        }
        canvas.drawRoundRect(metaBoxRect, 6f, 6f, metaBgPaint)
        canvas.drawRoundRect(metaBoxRect, 6f, 6f, metaBorderPaint)

        // Metadata Columns
        val memberName = userProfile?.name ?: "Sovereign Initiate"
        val tierName = userProfile?.tierName ?: "Novice"
        val totalXp = userProfile?.xpTotal ?: 0
        val mindsetScore = userProfile?.mindsetScore ?: 50
        val dateString = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())

        val metaColY = y + 14f
        val metaValY = y + 27f

        val col1X = MARGIN_LEFT + 14f
        val col2X = MARGIN_LEFT + CONTENT_WIDTH * 0.35f
        val col3X = MARGIN_LEFT + CONTENT_WIDTH * 0.68f

        val metaLabelPaint = TextPaint().apply {
            color = COLOR_MUTED
            textSize = 7.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.08f
            isAntiAlias = true
        }

        val metaValuePaint = TextPaint().apply {
            color = COLOR_OBSIDIAN
            textSize = 9.5f
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            isAntiAlias = true
        }

        canvas.drawText("MEMBER / ARCHIVIST", col1X, metaColY, metaLabelPaint)
        canvas.drawText(memberName, col1X, metaValY, metaValuePaint)

        canvas.drawText("SOVEREIGN TIER & XP", col2X, metaColY, metaLabelPaint)
        canvas.drawText("$tierName Tier ($totalXp XP)", col2X, metaValY, metaValuePaint)

        canvas.drawText("DATE ARCHIVED & ENTRIES", col3X, metaColY, metaLabelPaint)
        canvas.drawText("$dateString • $entriesCount Inscriptions", col3X, metaValY, metaValuePaint)

        y += metaBoxHeight + 14f

        // Bottom divider rule
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + CONTENT_WIDTH, y, thinRulePaint)
        y += 14f

        return y
    }

    private fun drawRunningHeader(
        canvas: Canvas,
        pageNumber: Int,
        goldRulePaint: Paint,
        metaPaint: TextPaint
    ): Float {
        var y = MARGIN_TOP

        val headerPaint = TextPaint().apply {
            color = COLOR_MUTED
            textSize = 8f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            letterSpacing = 0.1f
            isAntiAlias = true
        }

        canvas.drawText("THE RICHES PROTOCOL • SOVEREIGN RITUAL ARCHIVE", MARGIN_LEFT, y, headerPaint)

        val pageLabelPaint = TextPaint().apply {
            color = COLOR_GOLD_DARK
            textSize = 8f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("PAGE $pageNumber", MARGIN_LEFT + CONTENT_WIDTH, y, pageLabelPaint)

        y += 6f
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + CONTENT_WIDTH, y, goldRulePaint)
        y += 16f

        return y
    }

    private fun drawEntryCard(
        canvas: Canvas,
        entry: NotebookEntryEntity,
        entryNumber: Int,
        formattedDate: String,
        titleLayout: StaticLayout,
        promptLayout: StaticLayout?,
        bodyLayout: StaticLayout,
        startY: Float,
        metaPaint: TextPaint,
        metaBoldPaint: TextPaint,
        titlePaint: TextPaint,
        promptPaint: TextPaint,
        bodyPaint: TextPaint
    ): Float {
        val cardPadding = 14f
        var cardInnerY = startY + cardPadding

        val cardHeight = cardPadding +
                16f + // Inscription badge & date row
                titleLayout.height +
                8f +
                (if (promptLayout != null) promptLayout.height + 10f else 0f) +
                bodyLayout.height +
                cardPadding

        val cardRect = RectF(MARGIN_LEFT, startY, MARGIN_LEFT + CONTENT_WIDTH, startY + cardHeight)

        // Card background
        val bgPaint = Paint().apply {
            color = COLOR_CARD_BG
            style = Paint.Style.FILL
        }
        val borderPaint = Paint().apply {
            color = if (entry.isFavorite) COLOR_GOLD_PRIMARY else COLOR_CARD_BORDER
            style = Paint.Style.STROKE
            strokeWidth = if (entry.isFavorite) 1.2f else 0.8f
        }

        canvas.drawRoundRect(cardRect, 8f, 8f, bgPaint)
        canvas.drawRoundRect(cardRect, 8f, 8f, borderPaint)

        // Left accent bar
        val accentBarWidth = if (entry.isFavorite) 4.5f else 3f
        val accentBarRect = RectF(MARGIN_LEFT, startY + 6f, MARGIN_LEFT + accentBarWidth, startY + cardHeight - 6f)
        val accentBarPaint = Paint().apply {
            color = if (entry.isFavorite) COLOR_GOLD_PRIMARY else COLOR_GOLD_LIGHT
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(accentBarRect, 2f, 2f, accentBarPaint)

        val contentLeft = MARGIN_LEFT + cardPadding + 4f

        // Top Row: INSCRIPTION # + TAGS + DATE + FAVORITE STAR
        val tagLabel = if (entry.tags.isNotBlank()) " • ${entry.tags.uppercase()}" else ""
        val entryTagText = "INSCRIPTION NO. $entryNumber$tagLabel"
        canvas.drawText(entryTagText, contentLeft, cardInnerY, metaBoldPaint)

        val starText = if (entry.isFavorite) "★ FAVORITE • $formattedDate" else formattedDate
        val rightAlignMeta = TextPaint(metaPaint).apply { textAlign = Paint.Align.RIGHT }
        canvas.drawText(starText, MARGIN_LEFT + CONTENT_WIDTH - cardPadding, cardInnerY, rightAlignMeta)

        cardInnerY += 14f

        // Title
        canvas.save()
        canvas.translate(contentLeft, cardInnerY)
        titleLayout.draw(canvas)
        canvas.restore()
        cardInnerY += titleLayout.height + 8f

        // Prompt Question (if present)
        if (promptLayout != null) {
            val promptBoxRect = RectF(
                contentLeft - 4f,
                cardInnerY - 2f,
                MARGIN_LEFT + CONTENT_WIDTH - cardPadding,
                cardInnerY + promptLayout.height + 4f
            )
            val promptBg = Paint().apply {
                color = COLOR_GOLD_FAINT
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(promptBoxRect, 4f, 4f, promptBg)

            // Prompt left mini-indicator
            val promptLinePaint = Paint().apply {
                color = COLOR_GOLD_PRIMARY
                strokeWidth = 2f
            }
            canvas.drawLine(contentLeft - 4f, cardInnerY - 2f, contentLeft - 4f, cardInnerY + promptLayout.height + 4f, promptLinePaint)

            canvas.save()
            canvas.translate(contentLeft + 4f, cardInnerY + 1f)
            promptLayout.draw(canvas)
            canvas.restore()
            cardInnerY += promptLayout.height + 10f
        }

        // Body Content
        canvas.save()
        canvas.translate(contentLeft, cardInnerY)
        bodyLayout.draw(canvas)
        canvas.restore()

        return startY + cardHeight
    }

    private fun drawPageFooter(
        canvas: Canvas,
        pageNumber: Int,
        thinRulePaint: Paint,
        metaPaint: TextPaint
    ) {
        val y = PAGE_HEIGHT - MARGIN_BOTTOM + 16f
        canvas.drawLine(MARGIN_LEFT, y, MARGIN_LEFT + CONTENT_WIDTH, y, thinRulePaint)

        val footerTextY = y + 14f

        val footerMottoPaint = TextPaint().apply {
            color = COLOR_MUTED
            textSize = 7.5f
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
            isAntiAlias = true
        }
        canvas.drawText("“Whatever the mind can conceive and believe, it can achieve.” — Napoleon Hill", MARGIN_LEFT, footerTextY, footerMottoPaint)

        val pagePaint = TextPaint().apply {
            color = COLOR_GOLD_DARK
            textSize = 8f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("PAGE $pageNumber", MARGIN_LEFT + CONTENT_WIDTH, footerTextY, pagePaint)
    }

    private fun createStaticLayout(
        text: CharSequence,
        paint: TextPaint,
        width: Int
    ): StaticLayout {
        val safeWidth = width.coerceAtLeast(50)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(text, 0, text.length, paint, safeWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(2f, 1.15f)
                .setIncludePad(false)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                text,
                paint,
                safeWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.15f,
                2f,
                false
            )
        }
    }

    /**
     * Triggers the Android Intent Chooser to view or share the generated PDF.
     */
    fun sharePdfDocument(context: Context, result: PdfExportResult, subject: String = "Sovereign Ritual Notebook Archive") {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, result.uri)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, "Here is my permanent Sovereign Ritual Notebook archive from The Riches Protocol (${result.totalEntries} entries).")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(intent, "Share Sovereign Notebook PDF").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(chooser)
    }

    /**
     * Directly opens the generated PDF in an installed PDF reader application.
     */
    fun openPdfDocument(context: Context, result: PdfExportResult) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(result.uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general share chooser if no default PDF viewer is registered
            sharePdfDocument(context, result)
        }
    }
}
