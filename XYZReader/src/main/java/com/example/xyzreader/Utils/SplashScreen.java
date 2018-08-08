package com.example.xyzreader.Utils;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.example.xyzreader.R;
import com.example.xyzreader.ui.main.ArticleListActivity;

public class SplashScreen extends AppCompatActivity implements RequestQueueInstance.HasDataArrived{
    private static final String mUdacityUrl = "https://go.udacity.com/xyz-reader-json";
    private CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        https://stackoverflow.com/questions/2868047/fullscreen-activity-in-android
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        cdt = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Check cache or Make request for data
                checkCacheOrRequest();
            }

            @Override
            public void onFinish() {
                sendIntentToMain();
            }
        };

        cdt.start();
    }

    private void checkCacheOrRequest() {
        if (RequestQueueInstance.getCachedData(this, mUdacityUrl) != null) {
//             Cached data is available
            // Simply move forward
            sendIntentToMain();
        } else {
            makeInternetRequest();
        }
    }

    private void makeInternetRequest() {
        RequestQueueInstance.getInstance(this).commonNetworkRequest(mUdacityUrl);
    }

    private void sendIntentToMain() {
        Intent openMain = new Intent(this, ArticleListActivity.class);
        startActivity(openMain);
        finish();
    }


    @Override
    public void isDataAvailable(boolean isAvailable) {
        if(isAvailable){
            cdt.cancel();
            sendIntentToMain();
        }
    }
}
