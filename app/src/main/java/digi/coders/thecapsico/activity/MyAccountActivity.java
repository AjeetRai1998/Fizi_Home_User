package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityMyAccountBinding;

public class MyAccountActivity extends AppCompatActivity {

    ActivityMyAccountBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}