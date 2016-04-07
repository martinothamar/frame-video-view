package com.mklimek.frameviedoview;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.Map;

public class VideoViewImpl extends VideoView implements Impl, MediaPlayer.OnPreparedListener {

    private View placeholderView;
    private Uri videoUri;
    private Map<String, String> headers;
    private FrameVideoViewListener listener;

    public VideoViewImpl(Context context) {
        super(context);
    }

    public VideoViewImpl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(View placeholderView, Uri videoUri, Map<String, String> headers) {
        this.placeholderView = placeholderView;
        this.videoUri = videoUri;
        this.headers = headers;
        setOnPreparedListener(this);
    }

    public VideoViewImpl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnPreparedListener(this);
    }

    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT >= 21)
            setVideoURI(videoUri, headers);
        else {
            try {
                Field field = VideoView.class.getDeclaredField("mHeaders");
                field.setAccessible(true);
                field.set(this,  headers);
            } catch (Exception e) {
            }
            setVideoURI(videoUri);
        }
        start();
    }

    @Override
    public void onPause() {
        placeholderView.setVisibility(View.VISIBLE);
        stopPlayback();
    }

    @Override
    public void setFrameVideoViewListener(FrameVideoViewListener listener) {
        this.listener = listener;
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnInfoListener(new InfoListener(placeholderView));
        if(listener != null){
           listener.mediaPlayerPrepared(mediaPlayer);
        }
    }
}



