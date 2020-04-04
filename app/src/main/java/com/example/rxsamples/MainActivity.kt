package com.example.rxsamples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Creation().latest()
        Transformation().latest()
        RandomThings().latest()
        Multicast().latest()
        Conditional().latest()
        Boolean().latest()
        Filtering().latest()
        Error().latest()
        Aggregation().latest()
    }
}
