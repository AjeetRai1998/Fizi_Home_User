package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityThankyouBinding;

public class ThankyouActivity extends AppCompatActivity {

    ActivityThankyouBinding binding;
    private String orderId;
    public static Activity ThankyouActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityThankyouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ThankyouActivity=this;
        orderId=getIntent().getStringExtra("orderId");
        binding.trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSummaryActivity.orderId=orderId;
                Intent in=new Intent(ThankyouActivity.this,OrderSummaryActivity.class);
                in.putExtra("sta",1);
                startActivity(in);
            }
        });
    }

    public void goToHome(View view) {
        startActivity(new Intent(this,DashboardActivity.class));
    }
}