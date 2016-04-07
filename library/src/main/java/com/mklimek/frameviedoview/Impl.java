package com.mklimek.frameviedoview;

import android.net.Uri;
import android.view.View;

import java.util.Map;

interface Impl {
    void init(View placeholderView, Uri videoUri, Map<String, String> headers);
    void onResume();
    void onPause();
    void setFrameVideoViewListener(FrameVideoViewListener listener);
}
