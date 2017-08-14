package com.dadaxueche.student.dadaapp.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.Util.FullScreenVideoView;

public class FullPlayActivity extends AppCompatActivity {
    private int currentposition;
    private FullScreenVideoView fullvideoview;
    private String nativepath;
    private RelativeLayout download_back;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_play);
        intent = getIntent();
        int width = intent.getIntExtra("width", 0);
        int height = intent.getIntExtra("height", 0);
        nativepath = intent.getStringExtra("nativepath");
        currentposition = intent.getIntExtra("currentposition", 0);
        fullvideoview = (FullScreenVideoView) findViewById(R.id.fullvideoview);
        fullvideoview.setVideoURI(Uri.parse(nativepath));
        fullvideoview.setMediaController(new MediaController(this));
        fullvideoview.setWidthandHeight(width, height);
        fullvideoview.seekTo(currentposition);
        fullvideoview.start();
        download_back = (RelativeLayout) findViewById(R.id.download_back);
        download_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentposition = fullvideoview.getCurrentPosition();
                intent.putExtra("currentposition", currentposition);
                setResult(123, intent);
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            currentposition = fullvideoview.getCurrentPosition();
            intent.putExtra("currentposition", currentposition);
            setResult(123, intent);
            finish();
        }
        return false;
    }
}
