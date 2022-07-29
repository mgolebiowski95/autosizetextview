package com.mgsoftware.autosizetextview

import android.content.Context
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.math.MathUtils
import kotlin.system.measureTimeMillis

/**
 * Created by Mariusz base on https://github.com/AndroidDeveloperLB/AutoFitTextView
 */
class AutoSizeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private val availableSpaceRect = RectF()
    private val sizeTester: SizeTester
    private var maxLines: Int = 0
    private val viewParams = ViewParams(context, attrs, defStyleAttr)

    private val isWrapContent: Boolean
        get() {
            val layoutParams = layoutParams
            return if (layoutParams != null)
                layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT
            else
                false
        }
    private var initialized = false

    init {
        if (maxLines == 0)
            maxLines = NO_LINE_LIMIT

        sizeTester = DefaultSizeTester(this, viewParams)
        initialized = true
    }

    override fun setAllCaps(allCaps: Boolean) {
        super.setAllCaps(allCaps)
        adjustTextSize()
    }

    override fun setTypeface(tf: Typeface?) {
        super.setTypeface(tf)
        adjustTextSize()
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
        adjustTextSize()
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setSingleLine() {
        super.setSingleLine()
        maxLines = 1
        adjustTextSize()
    }

    override fun setSingleLine(singleLine: Boolean) {
        super.setSingleLine(singleLine)
        maxLines = if (singleLine)
            1
        else
            NO_LINE_LIMIT
        adjustTextSize()
    }

    override fun setLines(lines: Int) {
        super.setLines(lines)
        maxLines = lines
        adjustTextSize()
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        adjustTextSize()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (isWrapContent) {
            if (height != oldHeight)
                adjustTextSize()
        } else {
            if (width != oldWidth || height != oldHeight)
                adjustTextSize()
        }
    }

    private fun adjustTextSize() {
        if (!initialized)
            return

        if (viewParams.measureTextSizeEnabled) {
            val executionTime = measureTimeMillis {
                val minTextSize = viewParams.minTextSizeFactor * measuredHeight
                val maxTextSize = viewParams.maxTextSizeFactor * measuredHeight

                var widthLimit = if (viewParams.skipLayoutWidth) {
                    Integer.MAX_VALUE
                } else {
                    measuredWidth - compoundPaddingLeft - compoundPaddingRight
                }
                if (maxWidth != Integer.MAX_VALUE)
                    widthLimit = MathUtils.clamp(widthLimit, minWidth, maxWidth)
                val heightLimit = measuredHeight - compoundPaddingBottom - compoundPaddingTop

                availableSpaceRect.right = widthLimit.toFloat()
                availableSpaceRect.bottom = heightLimit.toFloat()
                if (availableSpaceRect.isEmpty)
                    return

                val textSize = binarySearch(
                    minTextSize.toInt(),
                    maxTextSize.toInt(),
                    sizeTester,
                    availableSpaceRect
                )
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            }
            Log.d(TAG, "measuring text size: executionTime = $executionTime ms")
            post { requestLayout() }
        }
    }

    private fun binarySearch(
        start: Int,
        end: Int,
        sizeTester: SizeTester?,
        availableSpace: RectF
    ): Int {
        if (DEBUG)
            Log.d(
                TAG,
                "binarySearch() called with: start = [$start], end = [$end], sizeTester = [$sizeTester], availableSpace = [$availableSpace]"
            )
        val loopCounter = LoopCounter()
        var lastBest = start
        var lo = start
        var hi = end - 1
        var mid: Int
        while (lo <= hi) {
            mid = (lo + hi).ushr(1)
            val midValCmp = sizeTester!!.onTestSize(mid, availableSpace)
            if (midValCmp < 0) {
                lastBest = lo
                lo = mid + 1
            } else if (midValCmp > 0) {
                hi = mid - 1
                lastBest = hi
            } else {
                return mid
            }
            loopCounter.loop()
        }
        if (DEBUG)
            Log.d(
                TAG,
                "binarySearch() result: loopCount = [" + loopCounter.count + "], textSize = [" + lastBest + "]"
            )
        // make sure to return last best
        // this is what should always be returned
        return lastBest
    }

    private inner class LoopCounter {
        var count: Int = 0
            private set

        fun loop() {
            count++
        }
    }

    inner class ViewParams(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) {
        var measureTextSizeEnabled: Boolean = true
        var skipLayoutWidth = false
        var minTextSizeFactor: Float = 0f
        var maxTextSizeFactor: Float = 1f
        var referenceText: String? = null

        init {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.AutoSizeTextView,
                defStyleAttr,
                0
            )
            measureTextSizeEnabled = a.getBoolean(
                R.styleable.AutoSizeTextView_AutoSizeTextView_measureTextSizeEnabled,
                measureTextSizeEnabled
            )
            skipLayoutWidth =
                a.getBoolean(
                    R.styleable.AutoSizeTextView_AutoSizeTextView_skipLayoutWidth,
                    skipLayoutWidth
                )
            minTextSizeFactor =
                a.getFloat(
                    R.styleable.AutoSizeTextView_AutoSizeTextView_minTextSizeFactor,
                    minTextSizeFactor
                )
            maxTextSizeFactor =
                a.getFloat(
                    R.styleable.AutoSizeTextView_AutoSizeTextView_maxTextSizeFactor,
                    maxTextSizeFactor
                )
            if (a.hasValue(
                    R.styleable.AutoSizeTextView_AutoSizeTextView_referenceText
                )
            )
                referenceText =
                    a.getString(R.styleable.AutoSizeTextView_AutoSizeTextView_referenceText)
            a.recycle()
        }
    }

    companion object {
        private const val TAG = "AutoSizeTextView"
        private const val DEBUG = true

        private const val NO_LINE_LIMIT = -1
    }
}
