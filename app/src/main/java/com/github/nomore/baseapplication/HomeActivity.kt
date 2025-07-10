package com.github.nomore.baseapplication

import com.github.nomore.base.BaseActivity
import com.github.nomore.base.utils.getIntentParcelable
import com.github.nomore.base.utils.returnData
import com.github.nomore.base.utils.toastMessage
import com.github.nomore.baseapplication.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onActivityCreated() {

    }
}