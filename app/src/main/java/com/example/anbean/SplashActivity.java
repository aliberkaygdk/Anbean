package com.example.anbean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    /*FirebaseUser firebaseUser;
    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView text=findViewById(R.id.textView);
        text.setVisibility(View.VISIBLE);
        Thread logoAnimation= new Thread(){
            @Override
            public void run(){
                ImageView logo=findViewById(R.id.imageView2);
                Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_intro_logo);
                logo.startAnimation(animation);

            }
        };
        logoAnimation.start();

        Thread textAnimation=new Thread(){
            @Override
            public void run(){


                TextView text=findViewById(R.id.textView);
                Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_intro_text);
                text.startAnimation(animation2);
            }
        };
        textAnimation.start();
        Thread redirect=new Thread(){
            public void run(){
                try {
                    sleep(4000);
                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        redirect.start();
    }
}