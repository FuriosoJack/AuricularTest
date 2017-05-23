package com.furiosojack.audifontest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.furiosojack.audifontest.Activitys.MainActivity;

/**
 * Created by juan0 on 8/12/16.
 */

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       Intent main = new Intent(this,MainActivity.class);
        startActivity(main);
        finish();

    }
}
