package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import digi.coders.thecapsico.databinding.ActivityPaymentFailureBinding;
import digi.coders.thecapsico.databinding.ActivityPaymentModeBinding;

public class PaymentFailureActivity extends AppCompatActivity {
    ActivityPaymentFailureBinding binding;
    public static Activity PaymentFailureActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPaymentFailureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //
        PaymentFailureActivity=this;
        binding.reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFailureActivity.this, ViewOrderActivity.class));
                finish();
            }
        });

        binding.goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentFailureActivity.this, DashboardActivity.class));
                finish();
            }
        });

    }
}