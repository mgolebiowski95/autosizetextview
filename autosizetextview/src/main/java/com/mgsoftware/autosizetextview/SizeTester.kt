package com.mgsoftware.autosizetextview

import android.graphics.RectF

interface SizeTester {

    fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int
}