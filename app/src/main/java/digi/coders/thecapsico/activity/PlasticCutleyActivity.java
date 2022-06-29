package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.databinding.ActivityPlasticCutleyBinding;

public class PlasticCutleyActivity extends AppCompatActivity {

    ActivityPlasticCutleyBinding binding;
    int quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPlasticCutleyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //add cart
        binding.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlasticCutleyActivity.this, "cart added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        quantity=Integer.parseInt(binding.quantityTxt.getText().toString());
        if(quantity>0) {
            binding.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(quantity>1)
                    {
                     quantity--;
                     binding.quantityTxt.setText(quantity+"");
                    }
                }
            });
            binding.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    binding.quantityTxt.setText(quantity+"");
                }
            });
        }

    }
}