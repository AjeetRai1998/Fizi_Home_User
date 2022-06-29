package digi.coders.thecapsico.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.json.JSONObject;

import digi.coders.thecapsico.R;
import digi.coders.thecapsico.adapter.ChooseAddressAdapter;
import digi.coders.thecapsico.databinding.ActivityCartBinding;
import digi.coders.thecapsico.databinding.CartItemLayoutBinding;

public class CartActivity extends AppCompatActivity   {

    ActivityCartBinding binding;
    CartItemLayoutBinding binding1;
    public static Activity CartActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CartActivity=this;
        binding1=binding.cartLayout;
        quantityControl();
        binding.checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startPayment();
                startActivity(new Intent(CartActivity.this,PaymentModeActivity.class));
            }
        });
        binding.clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layout.setVisibility(View.GONE);
                binding.cartEmptyLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CouponActivity.class));
            }
        });
        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,ManageAddressActivity.class));
            }
        });
        binding.browseRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, BrowesStoreActivity.class));
            }
        });
        binding.cartEmptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layout.setVisibility(View.GONE);
                binding.cartEmptyLayout.setVisibility(View.VISIBLE);

            }
        });




        //handle tip

    //loadAddressList();
    }


    private void quantityControl() {

        binding1.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(binding1.quantityTextView.getText().toString());
                if(qty<1000){

                    //holder.plusbutton.setVisibility(View.INVISIBLE );
                    qty++;
                    binding1.quantityTextView.setText(qty+"");
                }
            }});
        binding1.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(binding1.quantityTextView.getText().toString());
                if(qty<1)
                {
                   binding.layout.setVisibility(View.GONE);
                    binding.cartEmptyLayout.setVisibility(View.VISIBLE);
                }
                if(qty>0){
                    //holder.minusbutton.setVisibility( View.INVISIBLE );
                    qty--;
                    binding1.quantityTextView.setText(qty+"");

                }
            }
        });

    }

/*    private void loadAddressList() {
        binding.addressList.setLayoutManager(new LinearLayoutManager(CartActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.addressList.setAdapter(new ChooseAddressAdapter());
    }*/


}