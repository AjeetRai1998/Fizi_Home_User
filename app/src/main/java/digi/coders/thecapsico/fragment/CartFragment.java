package digi.coders.thecapsico.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import digi.coders.thecapsico.R;
import digi.coders.thecapsico.activity.BrowesStoreActivity;
import digi.coders.thecapsico.activity.CouponActivity;
import digi.coders.thecapsico.activity.ManageAddressActivity;
import digi.coders.thecapsico.adapter.ChooseAddressAdapter;

public class CartFragment extends Fragment  {

    private RelativeLayout checkout;
    private CardView appluCoupon;
    private RelativeLayout addAddress;
    private RecyclerView addressList;
    private ImageView plusbutton,minusbutton;
    private TextView quantity_text_view,clearCart;
    private LinearLayout layout,cart_empty_layout;
    private MaterialButton browseRestaurant;
    private CardView twenty,thirty,fifty,other;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiView(view);
        loadAddressList();
        quantityControl();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startPayment();
            }
        });
        appluCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CouponActivity.class));
            }
        });
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ManageAddressActivity.class));
            }
        });
        browseRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BrowesStoreActivity.class));
            }
        });
        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                cart_empty_layout.setVisibility(View.VISIBLE);

            }
        });




        //handle tip

        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void quantityControl() {

        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity_text_view.getText().toString());
                if(qty<1000){

                    //holder.plusbutton.setVisibility(View.INVISIBLE );
                    qty++;
                    quantity_text_view.setText(qty+"");
                }
            }});
        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(quantity_text_view.getText().toString());
                if(qty<1)
                {

                    layout.setVisibility(View.GONE);
                    cart_empty_layout.setVisibility(View.VISIBLE);
                }
                if(qty>0){
                    //holder.minusbutton.setVisibility( View.INVISIBLE );
                    qty--;
                    quantity_text_view.setText(qty+"");

                }
            }
        });

    }
  /*  String api_key;
    private void startPayment() {
       *//* String jsonObject = singleTask.getValue("user");
        user = new Gson().fromJson(jsonObject, User.class);
        mobileNo = user.getNumber();*//*
        //Log.e("user",user.getNumber());
        //Log.e("sds",singleTask.getValue("mobile"));
        if (api_key == null) {
            //Log.e("api_key", api_key);
            final com.razorpay.Checkout co = new Checkout();
            //co.setKeyID(api_key);
            try {
                JSONObject options = new JSONObject();
                options.put("name", "Atul");
                *//*if (key.equals("1")) {
                    options.put("description", "Payment  For " + course.getName() + "Course at  CodersAdda.com");
                }
                if (key.equals("2")) {
                    options.put("description", "Payment  For " + ebook.getName() + "Course at  CodersAdda.com");
                }*//*
                options.put("description", "Payment  For " + "Order" + "Food from Capsico");
                options.put("image", "");
                options.put("currency", "INR");
                options.put("theme.color", "#FBA510");
                //Log.e("asdsad",ORDER_ID);
                options.put("order_id", "");
                //String d = orderTotalPrice.getText().toString().substring(1);
                //Log.e("sd",d);
                //Log.e("sds",d);
                //Log.e("sds", Double.parseDouble("23") * 100.0 + "");
                options.put("amount", Double.parseDouble("23") * 100.0);
                JSONObject preFill = new JSONObject();
                preFill.put("email", "atul@gmail.com");
                preFill.put("contact", "8707639833");
                options.put("prefill", preFill);
                *//*       progressDialog.hide();*//*
                co.open(getActivity(), options);

            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.e("Error in payment", e.getMessage());
                e.printStackTrace();
            }
        } else {
            //getRezorPayAPiKey();
        }
    }
*/
    private void loadAddressList() {
        addressList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        addressList.setAdapter(new ChooseAddressAdapter());
    }

    private void initiView(View view) {
        checkout=view.findViewById(R.id.check_out);
        appluCoupon=view.findViewById(R.id.coupon_layout);
        addAddress=view.findViewById(R.id.addAddress);
        addressList=view.findViewById(R.id.address_list);
        plusbutton =view.findViewById( R.id.pulsbutton );
        minusbutton=view.findViewById( R.id.minusbutton );
        quantity_text_view=view.findViewById(R.id.quantity_text_view);
        cart_empty_layout=view.findViewById(R.id.cart_empty_layout);
        layout=view.findViewById(R.id.layout);
        browseRestaurant=view.findViewById(R.id.browse_restaurants);
        clearCart=view.findViewById(R.id.clear_cart);
        twenty=view.findViewById(R.id.twentyRupees);
        thirty=view.findViewById(R.id.thirtyRupees);
        fifty=view.findViewById(R.id.fiftyRupees);
        other=view.findViewById(R.id.other);

    }
}