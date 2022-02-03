package com.example.bloodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView slogan,title;
    Animation topAnimation, sideAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        slogan = findViewById(R.id.slogan);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        sideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_animation);

        imageView.setAnimation(topAnimation);
        title.setAnimation(sideAnimation);
        slogan.setAnimation(sideAnimation);

        int SPLASH_SCREEN = 4300;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);
    }
}