package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityManageAccountBinding;

public class ManageAccountActivity extends AppCompatActivity {

    ActivityManageAccountBinding binding;
    public static Activity ManageAccountActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ManageAccountActivity=this;
        //hanle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //editProfile
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(ManageAccountActivity.this,EditAccountActivity.class));

            }
        });

        //Manage Address
        binding.manageAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAccountActivity.this,ManageAddressActivity.class));
            }
        });

    }
}