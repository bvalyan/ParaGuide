package com.optimalotaku.paraguide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;

/**
 * Created by Brandon on 1/13/17.
 */

public class newsView extends Fragment {
    private WebView newsView;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };

    public static newsView newInstance() {
        
        Bundle args = new Bundle();
        
        newsView fragment = new newsView();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        View view = inflater.inflate(R.layout.news, container, false);

        this.newsView = (WebView) view.findViewById(R.id.newsview);

        newsView.loadUrl("https://www.epicgames.com/paragon/en-US/news");

        newsView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && newsView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }


        });


        return view;

    }

    private void webViewGoBack(){
        newsView.goBack();
    }
}
