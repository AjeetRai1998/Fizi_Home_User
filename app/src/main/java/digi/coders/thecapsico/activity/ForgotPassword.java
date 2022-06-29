package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import digi.coders.thecapsico.R;

public class ForgotPassword extends AppCompatActivity {
    MaterialButton btn_recover;
    public static Activity ForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        ForgotPassword=this;
        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgotPassword.this,OtpActivity.class);
                intent.putExtra("key",1);
                startActivity(intent);
            }
        });


    }

    private void initView() {
        btn_recover=findViewById(R.id.btn_recover);
    }

    public void goBack(View view) {
        finish();
    }
}