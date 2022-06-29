package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    ActivityHelpBinding binding;
    public static Activity HelpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        HelpActivity=this;

    }

    public void goBack(View view) {
        finish();
    }
}