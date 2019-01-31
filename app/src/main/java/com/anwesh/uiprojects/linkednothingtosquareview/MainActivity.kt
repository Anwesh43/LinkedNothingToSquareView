package com.anwesh.uiprojects.linkednothingtosquareview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.nothingtosquareview.NothingToSquareView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NothingToSquareView.create(this)
    }
}
