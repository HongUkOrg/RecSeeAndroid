package com.example.user.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class PlayerProgress extends AppCompatActivity {
    FloatingActionButton buttonPlayPause;
    SeekBar seekBarProgress;
    MediaPlayer mediaPlayer;
    Handler seekHandler = new Handler();
    private int mediaFileLengthInMilliseconds;
    TextView title;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplayer);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        path = getIntent().getStringExtra("path");
        title=findViewById(R.id.name);
        title.setText(path);
        getInit();
    }

    public void getInit()
    {
        buttonPlayPause = (FloatingActionButton) findViewById(R.id.button1);
        seekBarProgress = (SeekBar)findViewById(R.id.seekBar2);
        seekBarProgress.setMax(99);
        mediaPlayer = new MediaPlayer();
        try {
            FileInputStream fs = null;
            fs = new FileInputStream(path);
            FileDescriptor fd = null;
            fd = fs.getFD();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekUpdation();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!mediaPlayer.isPlaying()){
            buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        }else {
            buttonPlayPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        }
        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    buttonPlayPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }else {
                    mediaPlayer.pause();
                    buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                }
                seekUpdation();
            }
        });
        seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(!mediaPlayer.isPlaying()){
                    int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * seekBarProgress.getProgress();
                    mediaPlayer.seekTo(playPositionInMillisecconds);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mediaPlayer.start();
                if(!mediaPlayer.isPlaying()){
                    buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                }else {
                    buttonPlayPause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
                seekUpdation();
            }
        });
    }

    private void seekUpdation()
    {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying())
        {
            Runnable notification = new Runnable()
            {
                public void run()
                {
                    seekUpdation();
                }
            };
            seekHandler.postDelayed(notification, 1000);
        }
        else {
            buttonPlayPause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mediaPlayer.stop();
    }
}
