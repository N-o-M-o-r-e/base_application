package com.github.nomore.baseapplication

import com.github.nomore.base.BaseActivity
import com.github.nomore.base.utils.getIntentParcelable
import com.github.nomore.base.utils.returnData
import com.github.nomore.base.utils.toastMessage
import com.github.nomore.baseapplication.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private var receivedTemplate: TemplateConfig? = null
    private var modifiedTemplate: TemplateConfig? = null

    override fun onActivityCreated() {
        // Nhận template từ MainActivity
        receivedTemplate = getIntentParcelable<TemplateConfig>(TemplateConfig.EXTRA_KEY)

        if (receivedTemplate != null) {
            displayReceivedTemplate()
            setupModificationOptions()
        } else {
            handleNoTemplate()
        }

        setupClickListeners()
    }

    private fun displayReceivedTemplate() {
        receivedTemplate?.let { template ->
            binding.apply {
                tvReceivedInfo.text = "📥 Template nhận từ MainActivity:"
                tvTemplateDetails.text = template.getDisplayInfo()
                tvTemplateFullInfo.text = """
                    ID: ${template.id}
                    Tên: ${template.name}
                    Mô tả: ${template.description}
                    Background: ${template.backgroundColor}
                    Text Color: ${template.textColor}
                    Font Size: ${template.fontSize}px
                    Enabled: ${template.isEnabled}
                    Tags: ${template.tags.joinToString(", ")}
                    Created: ${
                    java.text.SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        java.util.Locale.getDefault()
                    ).format(template.createdAt)
                }
                """.trimIndent()

                // Copy để modify
                modifiedTemplate = template.copy()

                toastMessage("✅ Đã nhận template: ${template.name}")
            }
        }
    }

    private fun setupModificationOptions() {
        binding.apply {
            btnModifyName.text = "Đổi Tên Template"
            btnModifyColors.text = "Đổi Màu Sắc"
            btnModifyFontSize.text = "Đổi Font Size"
            btnToggleEnabled.text = "Toggle Enable/Disable"
            btnAddTag.text = "Thêm Tag"
        }
    }

    private fun setupClickListeners() {
        binding.apply {

            // Modify template name
            btnModifyName.setOnClickListener {
                modifiedTemplate = modifiedTemplate?.copy(
                    name = "Modified: ${modifiedTemplate?.name} (from Home)",
                    description = "Template đã được chỉnh sửa tại HomeActivity"
                )
                updateModifiedDisplay()
                toastMessage("✏️ Đã đổi tên template")
            }

            // Modify colors
            btnModifyColors.setOnClickListener {
                val colors = listOf(
                    "#FF5722" to "#FFFFFF", // Red background, white text
                    "#4CAF50" to "#FFFFFF", // Green background, white text
                    "#2196F3" to "#FFFFFF", // Blue background, white text
                    "#FF9800" to "#000000", // Orange background, black text
                    "#9C27B0" to "#FFFFFF"  // Purple background, white text
                )

                val randomColor = colors.random()
                modifiedTemplate = modifiedTemplate?.copy(
                    backgroundColor = randomColor.first, textColor = randomColor.second
                )
                updateModifiedDisplay()
                toastMessage("🎨 Đã đổi màu sắc")
            }

            // Modify font size
            btnModifyFontSize.setOnClickListener {
                val newSize = (12..24).random()
                modifiedTemplate = modifiedTemplate?.copy(fontSize = newSize)
                updateModifiedDisplay()
                toastMessage("📏 Font size: ${newSize}px")
            }

            // Toggle enabled state
            btnToggleEnabled.setOnClickListener {
                modifiedTemplate = modifiedTemplate?.copy(
                    isEnabled = modifiedTemplate?.isEnabled == false
                )
                updateModifiedDisplay()
                toastMessage("🔄 Toggle enabled: ${modifiedTemplate?.isEnabled}")
            }

            // Add tag
            btnAddTag.setOnClickListener {
                val newTags = listOf("modified", "from-home", "updated", "custom", "enhanced")
                val randomTag = newTags.random()

                modifiedTemplate = modifiedTemplate?.copy(
                    tags = (modifiedTemplate?.tags ?: emptyList()) + randomTag
                )
                updateModifiedDisplay()
                toastMessage("🏷️ Đã thêm tag: $randomTag")
            }

            // Trả về template đã modify
            btnReturnModified.setOnClickListener {
                returnModifiedTemplate()
            }

            // Trả về template gốc
            btnReturnOriginal.setOnClickListener {
                returnOriginalTemplate()
            }

            // Cancel và không trả về gì
            btnCancel.setOnClickListener {
                finish() // Sẽ trigger onActivityResultCancelled() ở MainActivity
            }
        }
    }

    private fun updateModifiedDisplay() {
        modifiedTemplate?.let { template ->
            binding.apply {
                tvModifiedInfo.text = "✏️ Template sau khi chỉnh sửa:"
                tvModifiedDetails.text = template.getDisplayInfo()
                tvModifiedFullInfo.text = """
                    ID: ${template.id}
                    Tên: ${template.name}
                    Mô tả: ${template.description}
                    Background: ${template.backgroundColor}
                    Text Color: ${template.textColor}
                    Font Size: ${template.fontSize}px
                    Enabled: ${template.isEnabled}
                    Tags: ${template.tags.joinToString(", ")}
                """.trimIndent()

                // Update preview colors
                try {
                    cardModified.setCardBackgroundColor(android.graphics.Color.parseColor(template.backgroundColor))
                    tvModifiedDetails.setTextColor(android.graphics.Color.parseColor(template.textColor))
                    tvModifiedDetails.textSize = template.fontSize.toFloat()
                } catch (e: Exception) {
                    // Fallback nếu parse color fail
                }
            }
        }
    }

    private fun returnModifiedTemplate() {
        modifiedTemplate?.let { template ->
            // Validate template trước khi trả về
            if (template.isValid()) {
                returnData(
                    resultCode = RESULT_OK,
                    keyReturn = TemplateConfig.RESULT_KEY,
                    dataReturn = template
                )
                toastMessage("📤 Đã trả về template đã chỉnh sửa")
            } else {
                toastMessage("❌ Template không hợp lệ!")
            }
        } ?: run {
            toastMessage("❌ Không có template để trả về!")
        }
    }

    private fun returnOriginalTemplate() {
        receivedTemplate?.let { template ->
            returnData(
                resultCode = RESULT_OK, keyReturn = TemplateConfig.RESULT_KEY, dataReturn = template
            )
            toastMessage("📤 Đã trả về template gốc")
        } ?: run {
            toastMessage("❌ Không có template gốc!")
        }
    }

    private fun handleNoTemplate() {
        binding.apply {
            tvReceivedInfo.text = "❌ Không nhận được template từ MainActivity"
            tvTemplateDetails.text = "Vui lòng gửi template từ MainActivity"

            // Disable modification buttons
            btnModifyName.isEnabled = false
            btnModifyColors.isEnabled = false
            btnModifyFontSize.isEnabled = false
            btnToggleEnabled.isEnabled = false
            btnAddTag.isEnabled = false
            btnReturnModified.isEnabled = false
            btnReturnOriginal.isEnabled = false
        }

        toastMessage("⚠️ Không có template để xử lý")
    }
}