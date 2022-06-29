package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import digi.coders.thecapsico.R;

public class SignupActivity extends AppCompatActivity {
    private MaterialButton btn_signup;
    public static Activity SignupActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        SignupActivity=this;
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), OtpActivity.class);
                intent.putExtra("key",2);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {

        btn_signup=findViewById(R.id.btn_signup);
    }

    /*public void goBack(View view) {
        finish();
    }*/
}