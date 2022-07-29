package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.app.databinding.AutoSizeTextViewBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: AutoSizeTextViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.auto_size_text_view)
    }
}
