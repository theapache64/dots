package com.theah64.dots

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

/**
 * Created by theapache64 on 17/2/18.
 */

class Dots : LinearLayout {
    private var count = DEFAULT_COUNT
    private var lastActiveIndex: Int = 0
    private var inactiveColor: Int = 0
    private var activeColor: Int = 0
    private var speed: Int = 0
    private var radius: Int = 0
    private var margin: Int = 0

    private var isAnimating = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

        this.inactiveColor = ContextCompat.getColor(context, DEFAULT_INACTIVE_COLOR)
        this.activeColor = getAttributeColor(context, R.attr.colorPrimary)
        this.speed = DEFAULT_SPEED
        this.radius = DEFAULT_RADIUS
        this.margin = DEFAULT_MARGIN

        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.Dots, 0, 0)
            count = ta.getInt(R.styleable.Dots_count, DEFAULT_COUNT)
            inactiveColor = ta.getColor(R.styleable.Dots_inactive_dot_color, ContextCompat.getColor(context, DEFAULT_INACTIVE_COLOR))
            activeColor = ta.getColor(R.styleable.Dots_active_dot_color, activeColor)
            speed = ta.getInt(R.styleable.Dots_speed, DEFAULT_SPEED)
            radius = ta.getDimension(R.styleable.Dots_radius, DEFAULT_RADIUS.toFloat()).toInt()
            margin = ta.getDimension(R.styleable.Dots_margin, DEFAULT_MARGIN.toFloat()).toInt()
            ta.recycle()
        }

        setCount(count)

    }

    fun setActive(activeIndex: Int) {

        if (activeIndex >= count) {
            throw IllegalArgumentException("Invalid index$activeIndex , size is $count")
        }

        if (isAnimating) {
            Log.d("X", "Animation going on....")
            return
        }

        isAnimating = true

        //Setting current to inactive
        val currentDot = getChildAt(lastActiveIndex)
        val newDot = getChildAt(activeIndex)

        //Old go
        YoYo.with(Techniques.ZoomOut).duration(speed.toLong()).onEnd {
            (currentDot.background as GradientDrawable).setColor(inactiveColor)

            //Old come
            YoYo.with(Techniques.ZoomIn).duration(speed.toLong()).onEnd {
                //New go
                YoYo.with(Techniques.ZoomOut).duration(speed.toLong()).onEnd {
                    (newDot.background as GradientDrawable).setColor(inactiveColor)

                    //Setting active index
                    (newDot.background as GradientDrawable).setColor(activeColor)

                    //New come
                    YoYo.with(Techniques.ZoomIn).duration(speed.toLong()).onEnd { isAnimating = false }.playOn(newDot)
                }.playOn(newDot)
            }.playOn(currentDot)
        }.playOn(currentDot)


        lastActiveIndex = activeIndex
    }

    fun setCount(count: Int) {

        removeAllViews()

        this.count = count
        this.lastActiveIndex = 0

        val inflater = LayoutInflater.from(context)

        for (i in 0 until count) {

            val dot = inflater.inflate(R.layout.dot_row, this, false)
            val params = LinearLayout.LayoutParams(radius, radius)
            params.setMargins(margin, margin, margin, margin)
            dot.layoutParams = params

            val bg = dot.background as GradientDrawable
            if (i > 0) {
                //Not first item
                bg.setColor(inactiveColor)
            } else {

                bg.setColor(activeColor)
            }

            addView(dot)
        }

    }

    fun moveBack() {
        var temp = lastActiveIndex - 1

        if (temp == -1) {
            temp = count - 1
        }

        setActive(temp)
    }

    fun moveForward() {
        var temp = lastActiveIndex + 1

        if (temp == count) {
            temp = 0
        }

        setActive(temp)
    }

    companion object {

        private val DEFAULT_COUNT = 2
        private val DEFAULT_INACTIVE_COLOR = R.color.grey
        private val DEFAULT_SPEED = 100
        private val DEFAULT_RADIUS = 25
        private val DEFAULT_MARGIN = 8

        @ColorInt
        fun getAttributeColor(context: Context, @AttrRes colorAttribute: Int): Int {
            val attrs = intArrayOf(colorAttribute)
            val ta = context.obtainStyledAttributes(attrs)

            @ColorInt val color = ContextCompat.getColor(context,
                    ta.getResourceId(0, -1))
            ta.recycle()
            return color
        }
    }
}
