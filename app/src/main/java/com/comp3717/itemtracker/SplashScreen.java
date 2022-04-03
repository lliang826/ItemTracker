package com.comp3717.itemtracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            VideoView videoHolder = new VideoView(this);
            setContentView(videoHolder);
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.blueGrey));
            videoHolder.getParent();
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.item_tracker);
            videoHolder.setVideoURI(video);

            videoHolder.setOnCompletionListener(mp -> jump());
            videoHolder.start();
        } catch (Exception ex) {
            jump();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jump();
        return true;
    }

    private void jump() {
        if (isFinishing())
            return;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
