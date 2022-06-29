package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import digi.coders.thecapsico.databinding.ActivityPaymentManageBinding;

public class PaymentManageActivity extends AppCompatActivity {

    ActivityPaymentManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}