package com.example.garagesale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.example.garagesale.utils.ShareStorage;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ShareStorage.getLanguage(this).isEmpty()) {
            setAppLocale("en");
        } else {
            setAppLocale(ShareStorage.getLanguage(this));
        }

        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }

    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            conf.setLocale(new Locale(localeCode.toLowerCase()));
            Locale locale = new Locale(localeCode);
            Locale.setDefault(locale);
            conf.locale = locale;
        }
        else{
            conf.locale=new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(conf,dm);

        ShareStorage.setLanguage(this, localeCode);
    }
}
