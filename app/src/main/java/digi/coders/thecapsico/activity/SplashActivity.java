package digi.coders.thecapsico.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.helper.InternetCheck;
import digi.coders.thecapsico.model.User;
import digi.coders.thecapsico.singletask.SingleTask;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    ImageView logo;
    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        singleTask=(SingleTask)getApplication();

        logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(new InternetCheck().isNetworkAvailable(getApplicationContext())) {

                    String js=singleTask.getValue("user");
                    User user=new Gson().fromJson(js,User.class);
                    if(user!=null)
                    {
                        startActivity(new Intent(SplashActivity.this,DashboardActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(SplashActivity.this,NoInternetActivity.class));
                    finish();

                }

                //startActivity(new Intent(SplashActivity.this,LoginActivity.class));

                /*startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();*/
                //checkPermission();
            }
        },SPLASH_TIME_OUT);
    }

    private void initView() {
        logo=findViewById(R.id.logo);
    }

    private void checkPermission() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            //Toast.makeText(SplashActivity.this, "sdfghjkl", Toast.LENGTH_SHORT).show();

        }else {
            if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},500);
            }else {
                /*if (SharedPrefManagerLocation.isLoggedIn()){
                    Intent intent = new Intent(getApplicationContext(), LocationBannerActivity.class);
                    startActivity(intent);
                    finish();

                }else {*/
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                //
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==500){
            /*if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                Intent intent = new Intent(getApplicationContext(), LocationBannerActivity.class);
                startActivity(intent);
                finish();

            }else {*/
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //checkPermission();
    }

}