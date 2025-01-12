package com.umc.timeCAlling.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController

abstract class BaseActivity<V : ViewDataBinding>(@LayoutRes val layoutResource: Int) :
    AppCompatActivity() {

    private var _binding: V? = null
    protected val binding: V get() = _binding!!
    abstract fun initView()
    protected abstract fun initObserver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResource)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initObserver()
        initView()
    }
}