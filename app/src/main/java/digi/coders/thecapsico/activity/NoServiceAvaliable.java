package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityNoServiceAvaliableBinding;

public class NoServiceAvaliable extends AppCompatActivity {

    ActivityNoServiceAvaliableBinding binding;
    public static Activity NoServiceAvaliable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoServiceAvaliableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NoServiceAvaliable=this;
        Glide.with(this)
                .load(R.raw.location_gif)
                .into(binding.locationGif);

        binding.browserOtherLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(NoServiceAvaliable.this,DeliveryLocationActivity.class);
                in.putExtra("key",3);
                startActivity(in);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(DashboardActivity.refresh!=null){
            DashboardActivity.refresh.onRefresh();
        }
    }
}