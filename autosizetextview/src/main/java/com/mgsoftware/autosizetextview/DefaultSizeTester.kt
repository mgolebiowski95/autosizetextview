package com.mgsoftware.autosizetextview

import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.TextView

class DefaultSizeTester(
    private val textView: TextView,
    private val viewParams: AutoSizeTextView.ViewParams
) : SizeTester {
    private val textPaint = TextPaint()
    private val textRect = RectF()

    override fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int {
        val transformationMethod = textView.transformationMethod
        val source: CharSequence = viewParams.referenceText ?: textView.text
        val text: String =
            transformationMethod?.getTransformation(source, textView)
                ?.toString()
                ?: source.toString()

        val singleLine = textView.maxLines == 1
        if (singleLine) {
            textRect.bottom = textPaint.fontSpacing
            textRect.right = textPaint.measureText(text)
        } else {
            textPaint.reset()
            textPaint.set(textView.paint)
            textPaint.textSize = suggestedSize.toFloat()
            val layout: StaticLayout = run {
                val layoutBuilder = StaticLayout.Builder.obtain(
                    text,
                    0,
                    text.length,
                    textPaint,
                    availableSpace.width().toInt()
                )
                layoutBuilder.setLineSpacing(
                    textView.lineSpacingExtra,
                    textView.lineSpacingMultiplier
                )
                layoutBuilder.setAlignment(Layout.Alignment.ALIGN_NORMAL)
                layoutBuilder.setIncludePad(textView.includeFontPadding)
                layoutBuilder.build()
            }

            // return early if we have more lines
            if (textView.maxLines != NO_LINE_LIMIT && layout.lineCount > textView.maxLines)
                return 1
            textRect.bottom = layout.height.toFloat()
            var maxWidth = -1
            val lineCount = layout.lineCount
            for (i in 0 until lineCount) {
                val end = layout.getLineEnd(i)
                if (i < lineCount - 1 && end > 0 && !isValidWordWrap(text[end - 1]))
                    return 1
                if (maxWidth < layout.getLineRight(i) - layout.getLineLeft(i))
                    maxWidth = layout.getLineRight(i).toInt() - layout.getLineLeft(i).toInt()
            }
            textRect.right = maxWidth.toFloat()
        }

        textRect.offsetTo(0f, 0f)
        return if (availableSpace.contains(textRect)) -1 else 1
        // else, too big
    }

    private fun isValidWordWrap(c: Char): Boolean {
        return c == ' ' || c == '-'
    }

    companion object {
        private const val NO_LINE_LIMIT = -1
    }
}