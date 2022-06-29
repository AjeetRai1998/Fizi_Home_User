package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import digi.coders.thecapsico.databinding.ActivityBookingTablesBinding;

public class BookingTablesActivity extends AppCompatActivity {

    ActivityBookingTablesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBookingTablesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //handle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.bookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(BookingTablesActivity.this, "Table booked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}