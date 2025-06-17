package com.github.nomore.baseapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TemplateConfig(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val backgroundColor: String = "#FFFFFF",
    val textColor: String = "#000000",
    val fontSize: Int = 16,
    val isEnabled: Boolean = true,
    val tags: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable {

    companion object {
        const val EXTRA_KEY = "TEMPLATE_CONFIG"
        const val RESULT_KEY = "TEMPLATE_CONFIG_RESULT"

        // Tạo template mặc định
        fun createDefault(): TemplateConfig {
            return TemplateConfig(
                id = 1,
                name = "Default Template",
                description = "This is a default template",
                backgroundColor = "#F5F5F5",
                textColor = "#333333",
                fontSize = 14,
                isEnabled = true,
                tags = listOf("default", "template")
            )
        }

        // Tạo template tùy chỉnh
        fun createCustom(
            name: String,
            description: String,
            bgColor: String = "#FFFFFF",
            textColor: String = "#000000"
        ): TemplateConfig {
            return TemplateConfig(
                id = (1..1000).random(),
                name = name,
                description = description,
                backgroundColor = bgColor,
                textColor = textColor,
                fontSize = 16,
                isEnabled = true,
                tags = listOf("custom", "user-created")
            )
        }
    }

    // Helper methods
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                backgroundColor.startsWith("#") &&
                textColor.startsWith("#") &&
                fontSize > 0
    }

    fun getDisplayInfo(): String {
        return "Template: $name\nDescription: $description\nEnabled: $isEnabled"
    }
}
