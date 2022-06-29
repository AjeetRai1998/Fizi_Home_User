package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityAddAddressBinding;

public class AddAddressActivity extends AppCompatActivity {


    ActivityAddAddressBinding binding;
    public static Activity AddAddressActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AddAddressActivity=this;

        //handle back
       /* binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
    }
}