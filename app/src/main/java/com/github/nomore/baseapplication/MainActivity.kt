package com.github.nomore.baseapplication

import android.content.Intent
import android.graphics.Color
import com.github.nomore.base.BaseActivity
import com.github.nomore.base.utils.logI
import com.github.nomore.base.utils.toastMessage
import com.github.nomore.baseapplication.databinding.ActivityMainBinding
import androidx.core.graphics.toColorInt

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private var currentTemplate: TemplateConfig? = null

    override fun onActivityCreated() {
        setupViews()
        setupClickListeners()

        // Hiển thị template mặc định
        currentTemplate = TemplateConfig.createDefault()
        displayCurrentTemplate()
    }

    private fun setupViews() {
        // Setup initial UI state
        binding.apply {
            btnSendToHome.text = "Gửi Template tới Home"
            btnCreateCustom.text = "Tạo Template Tùy Chỉnh"
            tvCurrentTemplate.text = "Chưa có template"
        }
    }

    private fun setupClickListeners() {
        binding.apply {

            // Gửi template hiện tại tới HomeActivity
            btnSendToHome.setOnClickListener {
                currentTemplate?.let { template ->
                    goToActivityForResult<HomeActivity>(
                        key = TemplateConfig.EXTRA_KEY,
                        data = template
                    )
                } ?: run {
                    toastMessage("Chưa có template để gửi!")
                }
            }

            // Tạo template tùy chỉnh và gửi
            btnCreateCustom.setOnClickListener {
                val customTemplate = TemplateConfig.createCustom(
                    name = "Custom Template ${System.currentTimeMillis()}",
                    description = "Template được tạo từ MainActivity",
                    bgColor = "#E3F2FD",
                    textColor = "#1976D2"
                )

                currentTemplate = customTemplate
                displayCurrentTemplate()

                // Gửi ngay template vừa tạo
                goToActivityForResult<HomeActivity>(
                    key = TemplateConfig.EXTRA_KEY,
                    data = customTemplate
                )
            }

            // Reset template về mặc định
            btnReset.setOnClickListener {
                currentTemplate = TemplateConfig.createDefault()
                displayCurrentTemplate()
                toastMessage("Đã reset về template mặc định")
            }
        }
    }

    private fun displayCurrentTemplate() {
        currentTemplate?.let { template ->
            binding.apply {
                tvCurrentTemplate.text = template.getDisplayInfo()
                tvTemplateId.text = "ID: ${template.id}"
                tvTemplateName.text = "Tên: ${template.name}"
                tvTemplateDesc.text = "Mô tả: ${template.description}"
                tvTemplateColors.text = "Background: ${template.backgroundColor}\nText: ${template.textColor}"
                tvTemplateFontSize.text = "Font Size: ${template.fontSize}px"
                tvTemplateTags.text = "Tags: ${template.tags.joinToString(", ")}"

                // Thay đổi background color theo template
                try {
                    cardTemplate.setCardBackgroundColor(template.backgroundColor.toColorInt())
                    tvCurrentTemplate.setTextColor(Color.parseColor(template.textColor))
                } catch (e: Exception) {
                    // Fallback colors nếu parse color fail
                }
            }
        }
    }

    // ==================== Activity Result Callbacks ====================

    override fun onActivityResultReceived(data: Intent?) {
        // Nhận template đã được chỉnh sửa từ HomeActivity
        val modifiedTemplate = data?.getParcelableExtra<TemplateConfig>(TemplateConfig.RESULT_KEY)

        if (modifiedTemplate != null) {
            currentTemplate = modifiedTemplate
            displayCurrentTemplate()

            toastMessage("✅ Đã nhận template từ HomeActivity!")

            // Log thông tin template nhận được
            logTemplateInfo("Received from HomeActivity", modifiedTemplate)

        } else {
            toastMessage("⚠️ Không nhận được template từ HomeActivity")
        }
    }

    override fun onActivityResultCancelled() {
        toastMessage("❌ HomeActivity đã bị hủy")
    }

    override fun onActivityResultError(resultCode: Int, data: Intent?) {
        toastMessage("💥 Có lỗi xảy ra với code: $resultCode")
    }

    // ==================== Helper Methods ====================

    private fun logTemplateInfo(action: String, template: TemplateConfig) {
        logI("MainActivity", """
            $action:
            - ID: ${template.id}
            - Name: ${template.name}
            - Description: ${template.description}
            - Background: ${template.backgroundColor}
            - Text Color: ${template.textColor}
            - Font Size: ${template.fontSize}
            - Enabled: ${template.isEnabled}
            - Tags: ${template.tags}
            - Valid: ${template.isValid()}
        """.trimIndent())
    }
}