package com.github.nomore.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.github.nomore.base.utils.StatusBarState
import com.github.nomore.base.utils.createActivityResultLauncher
import com.github.nomore.base.utils.getActivityName
import com.github.nomore.base.utils.launchActivity
import com.github.nomore.base.utils.launchActivityForResult
import com.github.nomore.base.utils.logI
import com.github.nomore.base.utils.setupEdgeToEdgeWithDistinctBars

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseActivity<Binding : ViewBinding>(private val inflate: Inflate<Binding>) : AppCompatActivity() {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = _binding ?: throw IllegalStateException("Binding can only be used after onCreate() and before onDestroy()")

    // Activity Result Launcher - có thể override để custom callbacks
    protected open val activityResultLauncher: ActivityResultLauncher<Intent> by lazy {
        createActivityResultLauncher(
            onSuccess = { data -> onActivityResultReceived(data) },
            onCancelled = { onActivityResultCancelled() },
            onError = { resultCode, data -> onActivityResultError(resultCode, data) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        logI("*", "Activity : ${getActivityName(this)}")

        _binding = inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)
        setupEdgeToEdgeWithDistinctBars(binding.root, StatusBarState.Light)

        onActivityCreated()
    }

    protected abstract fun onActivityCreated()

    // ==================== Activity Result Helper Methods ====================

    /**
     * Mở Activity với kết quả trả về
     */
    protected fun goToNewActivityForResult(
        activity: Class<*>,
        key: String = "",
        data: Any? = null,
        flags: Int? = null
    ) {
        activityResultLauncher.launchActivity(this, activity, key, data, flags)
    }

    /**
     * Extension function với type safety cho Activity Result
     */
    protected inline fun <reified T : AppCompatActivity> goToActivityForResult(
        key: String = "",
        data: Any? = null,
        flags: Int? = null
    ) {
        activityResultLauncher.launchActivityForResult<T>(this, key, data, flags)
    }

    /**
     * Callback nhận dữ liệu từ Activity con khi thành công
     */
    protected open fun onActivityResultReceived(data: Intent?) {
        // Các Activity con có thể override để xử lý kết quả
    }

    /**
     * Callback khi Activity bị cancelled
     */
    protected open fun onActivityResultCancelled() {
        // Các Activity con có thể override để xử lý khi user cancel
    }

    /**
     * Callback khi có lỗi xảy ra
     */
    protected open fun onActivityResultError(resultCode: Int, data: Intent?) {
        // Các Activity con có thể override để xử lý error
        logI("BaseActivity", "Activity result error with code: $resultCode")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

/**
 * Các ví dụ sử dụng:
 *
 * class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
 *
 *     override fun onActivityCreated() {
 *         binding.btnOpenActivity.setOnClickListener {
 *             // Cách 1: Sử dụng function thông thường
 *             goToNewActivityForResult(
 *                 activity = SecondActivity::class.java,
 *                 key = "EXTRA_MESSAGE",
 *                 data = "Hello từ MainActivity!"
 *             )
 *
 *             // Cách 2: Sử dụng extension function với type safety
 *             goToActivityForResult<SecondActivity>(
 *                 key = "EXTRA_MESSAGE",
 *                 data = "Hello từ MainActivity!"
 *             )
 *
 *             // Cách 3: Sử dụng function từ FunctionKtx (không cần result)
 *             goToNewActivity(...)
 *
 *             // Cách 4: Thay đổi Window View configuration từ FunctionKtx
 *             setupFullScreenImmersive(binding.root)
 *             // hoặc
 *             setupEdgeToEdgeWithCoatingBars(binding.root)
 *             // hoặc với màu custom
 *             setupEdgeToEdgeWithCustomColor(
 *                 binding.root,
 *                 ContextCompat.getColor(this@MainActivity, R.color.primary_color),
 *                 isLightStatusBar = true
 *             )
 *         }
 *     }
 *
 *     override fun onActivityResultReceived(data: Intent?) {
 *         val message = getIntentData<String>("RESULT_MESSAGE") ?: "Không có dữ liệu"
 *         toastMessage(message)
 *     }
 * }
 *
 * =======================================================
 *
 * class SecondActivity : BaseActivity<ActivitySecondBinding>(ActivitySecondBinding::inflate) {
 *
 *     override fun onActivityCreated() {
 *         val message = getIntentData<String>("EXTRA_MESSAGE") ?: "Không có tin nhắn"
 *         binding.tvMessage.text = message
 *
 *         // Custom window configuration using FunctionKtx
 *         setupEdgeToEdgeWithCoatingBars(binding.root)
 *
 *         binding.btnSendResult.setOnClickListener {
 *             // Sử dụng function từ FunctionKtx
 *             returnData(
 *                 resultCode = RESULT_OK,
 *                 keyReturn = "RESULT_MESSAGE",
 *                 dataReturn = "Kết quả từ SecondActivity"
 *             )
 *         }
 *     }
 * }
 *
 * =======================================================
 *
 * // Sử dụng trực tiếp từ FunctionKtx (không cần BaseActivity)
 * class StandaloneActivity : AppCompatActivity() {
 *
 *     private lateinit var launcher: ActivityResultLauncher<Intent>
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(R.layout.activity_standalone)
 *
 *         // Setup window configuration từ FunctionKtx
 *         setupEdgeToEdgeWithDistinctBars(
 *             findViewById(R.id.root_view),
 *             StatusBarState.Light
 *         )
 *
 *         // Tạo launcher với callbacks
 *         launcher = createActivityResultLauncher(
 *             onSuccess = { data ->
 *                 val result = data?.getStringExtra("RESULT") ?: "No data"
 *                 toastMessage(result)
 *             },
 *             onCancelled = { toastMessage("Cancelled") },
 *             onError = { code, _ -> toastMessage("Error: $code") }
 *         )
 *
 *         // Launch activity
 *         launcher.launchActivityForResult<TargetActivity>(
 *             key = "MESSAGE",
 *             data = "Hello"
 *         )
 *
 *         // Hoặc thay đổi window configuration trong runtime
 *         findViewById<Button>(R.id.btnFullScreen).setOnClickListener {
 *             setupFullScreenImmersive(findViewById(R.id.root_view))
 *         }
 *     }
 * }
 */