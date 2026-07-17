package com.procinex.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.procinex.app.ui.CaptureFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, CaptureFragment())
                .commit()
        }
    }
}
