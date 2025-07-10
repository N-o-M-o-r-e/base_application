package com.github.nomore.baseapplication

import com.github.nomore.base.BaseActivity
import com.github.nomore.baseapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onActivityCreated() {

    }
}