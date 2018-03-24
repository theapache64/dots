package com.theah64.dotsexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.theah64.dots.Dots

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var dots: Dots? = null
    private val currentIndex = 0
    private val totalDots = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        this.dots = findViewById(R.id.dots)
        dots!!.setCount(totalDots)

        findViewById<View>(R.id.bMoveBack).setOnClickListener(this)
        findViewById<View>(R.id.bMoveForward).setOnClickListener(this)

    }


    override fun onClick(view: View) {


        when (view.id) {

            R.id.bMoveBack -> dots!!.moveBack()

            R.id.bMoveForward -> dots!!.moveForward()
        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
