package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.NotificationAdapter;

public class NotificationActivity extends AppCompatActivity {

    ImageView back;
    private RecyclerView notification;
    public static Activity NotificationActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        NotificationActivity=this;
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadNotification();
    }

    private void loadNotification() {
        notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,LinearLayoutManager.VERTICAL,false));
        notification.setAdapter(new NotificationAdapter());
    }

    private void initView() {
        notification=findViewById(R.id.notification);
    }

    public void goBack(View view) {
        finish();
    }
}