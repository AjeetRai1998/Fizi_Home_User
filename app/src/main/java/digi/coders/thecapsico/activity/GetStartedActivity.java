package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityGetStartedBinding;

public class GetStartedActivity extends AppCompatActivity {

    ActivityGetStartedBinding binding;
    public static Activity GetStartedActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGetStartedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GetStartedActivity=this;
        //handle sign up
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartedActivity.this,LoginActivity.class));
            }
        });
    }
}