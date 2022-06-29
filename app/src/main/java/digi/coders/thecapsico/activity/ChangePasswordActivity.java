package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import digi.coders.thecapsico.R;

public class ChangePasswordActivity extends AppCompatActivity {
    ImageView iv_back;
    public static Activity ChangePasswordActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ChangePasswordActivity=this;
        iv_back=findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}