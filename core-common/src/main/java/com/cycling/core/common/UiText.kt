package com.cycling.core.common

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    
    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText()
    
    data class Combined(
        val texts: List<UiText>,
        val separator: String = ""
    ) : UiText()
    
    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> {
            if (args.isEmpty()) {
                context.getString(resId)
            } else {
                context.getString(resId, *args.toTypedArray())
            }
        }
        is Combined -> texts.joinToString(separator) { it.asString(context) }
    }
    
    companion object {
        fun dynamic(value: String): UiText = DynamicString(value)
        
        fun resource(@StringRes resId: Int, vararg args: Any): UiText = 
            StringResource(resId, args.toList())
        
        fun combined(vararg texts: UiText, separator: String = ""): UiText = 
            Combined(texts.toList(), separator)
    }
}
