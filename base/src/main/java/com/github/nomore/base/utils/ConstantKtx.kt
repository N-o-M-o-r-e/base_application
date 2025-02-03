package com.github.nomore.base.utils

import android.content.Intent

val flagClearOldTask: Int
    get() = Intent.FLAG_ACTIVITY_CLEAR_TOP or
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
            Intent.FLAG_ACTIVITY_NEW_TASK