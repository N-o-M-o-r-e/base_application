package com.github.nomore.baseapplication

import com.github.nomore.base.BaseActivity
import com.github.nomore.base.utils.loadImageWithGlide
import com.github.nomore.baseapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun initAds() {

    }

    override fun initViewModel() {

    }

    override fun initData() {

    }

    override fun initView() {
        loadImageWithGlide(com.github.nomore.base.R.drawable.ic_place_holder, binding.imgTest)
    }

    override fun initAction() {

    }

}