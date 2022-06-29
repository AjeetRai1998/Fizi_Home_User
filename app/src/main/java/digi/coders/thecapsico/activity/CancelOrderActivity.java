package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityCancelOrderBinding;

public class CancelOrderActivity extends AppCompatActivity {

    ActivityCancelOrderBinding binding;
    public static Activity CancelOrderActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCancelOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CancelOrderActivity=this;
        Glide.with(this)
                .load(R.raw.cancel_order)
                .into(binding.animation);
        //handle to go home

        binding.goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CancelOrderActivity.this,DashboardActivity.class));
                finish();
            }
        });
    }
}