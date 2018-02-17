package com.theah64.dotsexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.theah64.dots.Dots;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Dots dots;
    private int currentIndex = 0;
    private final int totalDots = 8;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.dots = findViewById(R.id.dots);
        dots.setCount(totalDots);

        findViewById(R.id.bMoveBack).setOnClickListener(this);
        findViewById(R.id.bMoveForward).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.bMoveBack:
                dots.moveBack();
                break;

            case R.id.bMoveForward:
                dots.moveForward();
                break;
        }
    }
}
