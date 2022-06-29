package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.AddressAdapter;
import digi.coders.thecapsico.databinding.ActivityManageAddressBinding;

public class ManageAddressActivity extends AppCompatActivity {

    private RecyclerView addressList;
    private MaterialButton newAddress;
    ActivityManageAddressBinding binding;
    public static Activity ManageAddressActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityManageAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ManageAddressActivity=this;
        //load Address
        loadAddress();

        //handle new address
        binding.newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAddressActivity.this,AddAddressActivity.class));
            }
        });
    }

    private void loadAddress() {
        binding.addressList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.addressList.setAdapter(new AddressAdapter(2,null,null));
    }


    public void goBack(View view) {
        finish();
    }
}